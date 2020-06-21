import asyncio
import json
import os
from datetime import date, timedelta
from itertools import combinations
import aiohttp
from tqdm import tqdm
import logging

from CSV import MyCSV
from cityGet import cityGroup, partition

CSV_PATH = "E:\\_data\\dataset\\flight"
headers = {
    'Origin': 'https://www.lsjpjg.com',
    'Accept-Encoding': 'gzip, deflate, br',
    'Accept-Language': 'zh-CN,zh;q=0.9',
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3756.400 QQBrowser/10.5.4039.400',
    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
    'Accept': 'application/json, text/javascript, */*; q=0.01',
    'Referer': 'https://www.lsjpjg.com/search.html?dep_ct=%E5%8C%97%E4%BA%AC&arr_ct=%E4%B8%8A%E6%B5%B7&dep_dt=2019-06-01',
    'X-Requested-With': 'XMLHttpRequest',
}
qData = {
    'dep_dt': '2020-01-01',
    'dep_ct': '北京',
    'arr_ct': '成都'
}
LOG_FORMAT = "%(asctime)s - %(levelname)s - %(message)s"
logging.basicConfig(filename=CSV_PATH + '\\spider.log', level=logging.INFO, format=LOG_FORMAT)


class Task:
    def __init__(self, dep_dt, dep_ct, arr_ct):
        self.dep_dt = dep_dt
        self.dep_ct = dep_ct
        self.arr_ct = arr_ct

    def qDataDict(self):
        return {
            'dep_dt': self.dep_dt,
            'dep_ct': self.dep_ct,
            'arr_ct': self.arr_ct
        }

    def csvName(self):
        return {
            "dir": self.dep_ct + self.arr_ct,
            "csv": str(self.dep_dt)
        }

    def taskName(self):
        return self.dep_ct + self.arr_ct + str(self.dep_dt)


def parser(data_list: list):
    element = list()
    data = list()

    for item in data_list:
        if item["line"] == "noflight":
            return data
        element.append(item["line"])
        element.append(item["type"])
        element.append(item["dep_tm"])
        element.append(item["dep_ap"])
        element.append(item["arr_tm"])
        element.append(item["arr_ap"])
        element.append(item["discount"])
        element.append(item["price"])
        element.append(item["qry_dt"])
        element.append(item["fly_dt"])
        # print(element)
        data.append(element.copy())
        element.clear()

    # print(self.data)
    return data


def saveCSV(qData: Task, pData: list):
    # print(pData)
    dirPath = CSV_PATH + "\\" + qData.csvName()["dir"]
    if not os.path.isdir(dirPath):
        os.makedirs(dirPath)
    myCsv = MyCSV()
    myCsv.create(dirPath + "\\" + qData.csvName()["csv"])
    for item in pData:
        myCsv.insert_row(myCsv.get_max_row(), item)
    myCsv.save()


class Spider:
    def __init__(self, urlCrawl: str, dateStart: date, dateEnd: date, travelList: list):
        self.urlCrawl = urlCrawl
        self.taskQ = []

        self.dateRange = (dateStart, dateEnd)
        self.travelList = travelList
        delta = dateEnd - dateStart  # as timedelta
        for travel in travelList:
            for i in range(delta.days + 1):
                day = dateStart + timedelta(days=i)
                self.taskQ.append(Task(str(day), travel[0], travel[1]))

    def crawl(self):
        loop = asyncio.get_event_loop()
        loop.close()

        taskGroup = partition(self.taskQ, 10)
        pbar = tqdm(total=taskGroup.__len__())
        for tasks in taskGroup:
            pbar.set_description("Process " + tasks[0].taskName() + "-" + tasks[-1].taskName())
            loop = asyncio.new_event_loop()
            works = []
            for task in tasks:
                try:
                    works.append(self.startTask(task))
                except asyncio.CancelledError:
                    logging.error('Cancel the future.Task ' + task.taskName())
                except Exception as e:
                    logging.error('Cancel the future.Task ' + task.taskName())
            loop.run_until_complete(asyncio.wait(works))
            loop.close()
            pbar.update(1)
        pbar.close()

    async def startTask(self, qData: Task):
        url = self.urlCrawl
        data = qData.qDataDict()
        if url is None or len(url) == 0:
            logging.error("url为空或者不存在: " + qData.taskName())
            print("url为空或者不存在")
            return
        # print(data)
        try:
            async with aiohttp.ClientSession() as session:
                async with session.post(url, headers=headers, data=data) as resp:
                    text = await resp.text()
                    if text.startswith(u'\ufeff'):
                        text = text.encode('utf8')[3:].decode('utf8')
                    data_dic = json.loads(text)
                    for item in data_dic:
                        item.update({"fly_dt": data["dep_dt"]})
                    # print(data_dic)
                    sData = data_dic
        except aiohttp.ClientConnectorError:
            logging.error("aiohttp.ClientConnectorError: " + qData.taskName())
            print("\naiohttp.ClientConnectorError: " + qData.taskName())
            return

        pData = parser(sData)
        if len(pData) != 0:
            saveCSV(qData, pData)


def generateTravelList(cityList):
    return [combination for combination in combinations(cityList, 2)]


if __name__ == "__main__":
    # todo: change cityGroup index and CSV_PATH
    travelList = generateTravelList(cityGroup[0])
    sp = Spider(
        urlCrawl="https://www.lsjpjg.com/getthis.php",
        dateStart=date(2019, 4, 15),
        dateEnd=date(2020, 6, 21),
        travelList=travelList
    )
    sp.crawl()
