import csv


class MyCSV:
    def __init__(self):
        self.__file_name = ''
        self.__max_row = 0
        self.__max_col = 0
        self.__data_list = []

    # 插入空行
    def __insert_blank_row(self):
        if self.__max_row != 0 and self.__max_col != 0:
            row_data = [''] * self.__max_col
            self.__data_list.append(row_data)
            self.__max_row += 1
        else:
            print('\n表中数据为空,请先创建一个空单元格')

    # 插入空列
    def __insert_blank_column(self):
        if self.__max_row != 0 and self.__max_col != 0:
            col_data = [''] * self.__max_row
            for index in range(self.__max_row):
                self.__data_list[index].append(col_data[index])
            self.__max_col += 1
        else:
            print('\n表中数据为空,请先创建一个空单元格')

    # 创建一个单元格
    def __create_blank_cell(self):
        if self.__max_row == 0 and self.__max_col == 0:
            col_data = ['']
            self.__data_list.append(col_data)
            self.__max_row += 1
            self.__max_col += 1

    # 创建csv文件
    def create(self, file_name=''):
        if file_name == '':
            self.__file_name = '新建文件.csv'
        else:
            self.__file_name = file_name
        try:
            with open(self.__file_name, 'x') as outfile:
                print(self.__file_name, '文件创建成功')
            return True
        except FileExistsError:
            print(self.__file_name, '文件已存在')
            self.open(self.__file_name)
            return True
        except OSError:
            print(self.__file_name, '文件创建失败')
            return False

    # 打开csv文件
    def open(self, file_name):
        try:
            with open(file_name, 'r', newline='') as file:
                self.__file_name = file_name
                data = csv.reader(file)
                for row in data:
                    self.__max_row += 1
                    self.__max_col = len(row)
                    self.__data_list.append(row)
                if len(self.__data_list) == 1 and len(self.__data_list[0]) == 0:
                    self.__data_list.clear()
                    self.__max_col = 0
                    self.__max_row = 0
                print(file_name, '文件打开成功')
                return True
        except OSError:
            print(file_name, '文件打开失败')
            return False

    # 提取CSV中的行,返回列表
    def get_row(self, row_index):
        if self.__file_name == '':
            print('\n请先打开csv文件')
            return []
        elif self.__max_row == 0 or self.__max_col == 0:
            print('\n表格无数据')
            return []
        elif self.__max_row <= row_index:
            print('\n读取行号超出范围')
            return []
        else:
            return self.__data_list[row_index]

    # 提取CSV中的列,返回列表
    def get_column(self, col_index):
        if self.__file_name == '':
            print('\n请先打开csv文件')
            return []
        elif self.__max_row == 0 or self.__max_col == 0:
            print('\n表格无数据')
            return []
        elif self.__max_col <= col_index:
            print('\n读取列号超出范围')
            return []
        else:
            return [self.__data_list[index][col_index] for index in range(len(self.__data_list))]

    # 提取CSV单元格
    def get_cell(self, row_index, col_index):
        if self.__file_name == '':
            print('\n请先打开csv文件')
            return ''
        elif self.__max_row == 0 or self.__max_col == 0:
            print('\n表格无数据')
            return ''
        elif self.__max_row <= row_index:
            print('\n读取行号超出范围')
            return ''
        elif self.__max_col <= col_index:
            print('\n读取列号超出范围')
            return ''
        else:
            return self.__data_list[row_index][col_index]

    # 插入行
    def insert_row(self, row_index, row_data=[]):
        if self.__max_col == 0 and self.__max_row == 0:
            return self.set_row(row_index, row_data)
        temp_data = [temp for temp in row_data]
        if self.__file_name == '':
            print('\n请先打开csv文件')
            return False
        elif len(temp_data) == 0:
            print('\n插入数据为空')
            return False
        elif len(temp_data) == self.__max_col:
            self.__data_list.insert(row_index, temp_data)
            self.__max_row += 1
            return True
        elif len(temp_data) < self.__max_col:
            for index in range(len(temp_data), self.__max_col):
                temp_data.append('')
            self.__data_list.insert(row_index, temp_data)
            self.__max_row += 1
            return True
        else:
            for index in range(self.__max_col, len(temp_data)):
                self.__insert_blank_column()
            self.__data_list.insert(row_index, temp_data)
            self.__max_row += 1
            return True

    # 插入列
    def insert_column(self, col_index, col_data=[]):
        if self.__max_col == 0 and self.__max_row == 0:
            return self.set_column(col_index, col_data)
        temp_data = [temp for temp in col_data]
        if self.__file_name == '':
            print('\n请先打开csv文件')
            return False
        elif len(temp_data) == 0:
            print('\n插入数据为空')
            return False
        elif len(temp_data) == self.__max_row:
            for index in range(self.__max_row):
                self.__data_list[index].insert(col_index, temp_data[index])
            self.__max_col += 1
            return True
        elif len(temp_data) < self.__max_row:
            for index in range(len(temp_data), self.__max_row):
                temp_data.append('')
            for index in range(self.__max_row):
                self.__data_list[index].insert(col_index, temp_data[index])
            self.__max_col += 1
            return True
        else:
            for index in range(self.__max_row, len(temp_data)):
                self.__insert_blank_row()
            for index in range(self.__max_row):
                self.__data_list[index].insert(col_index, temp_data[index])
            self.__max_col += 1
            return True

    # 删除行
    def delete_row(self, row_index):
        if self.__file_name == '':
            print('\n请先打开csv文件')
            return False
        elif self.__max_row == 0 or self.__max_col == 0:
            print('\n表格无数据')
            return False
        elif self.__max_row <= row_index:
            print('\n删除行号超出范围')
            return False
        else:
            self.__data_list.pop(row_index)
            self.__max_row -= 1
            return True

    # 删除列
    def delete_column(self, col_index):
        if self.__file_name == '':
            print('\n请先打开csv文件')
            return False
        elif self.__max_row == 0 or self.__max_col == 0:
            print('\n表格无数据')
            return False
        elif self.__max_col <= col_index:
            print('\n删除列号超出范围')
            return False
        else:
            for index in range(self.__max_row):
                self.__data_list[index].pop(col_index)
            self.__max_col -= 1
            return True

    # 以col_index列为查找源，删除=key的行
    def delete_row_by_key(self, col_index, key):
        if self.__file_name == '':
            print('\n请先打开csv文件')
            return False
        elif self.__max_row == 0 or self.__max_col == 0:
            print('\n表格无数据')
            return False
        elif self.__max_col <= col_index:
            print('\n删除列号超出范围')
            return False
        index = self.get_row_index(col_index, key)
        if index == -1:
            print('\n在', col_index, '列找不到', key)
            return False
        else:
            return self.delete_row(index)

    # 以row_index列为查找源，删除=key的列
    def delete_column_by_key(self, row_index, key):
        if self.__file_name == '':
            print('\n请先打开csv文件')
            return False
        elif self.__max_row == 0 or self.__max_col == 0:
            print('\n表格无数据')
            return False
        elif self.__max_row <= row_index:
            print('\n删除行号超出范围')
        index = self.get_column_index(row_index, key)
        if index == -1:
            print('\n在', row_index, '行找不到', key)
        else:
            return self.delete_column(index)

    # 写行数据，覆盖原来的数据
    def set_row(self, row_index, row_data):
        temp_data = [temp for temp in row_data]
        if self.__file_name == '':
            print('\n请先打开csv文件')
            return False
        elif len(temp_data) == 0:
            print('\n写入数据为空')
            return False
        # 表格为空
        if self.__max_row == 0 and self.__max_col == 0:
            self.__create_blank_cell()
        if row_index >= self.__max_row:
            for index in range(self.__max_row, row_index + 1):
                self.__insert_blank_row()
        if len(temp_data) == self.__max_col:
            self.__data_list[row_index] = [temp for temp in temp_data]
            return True
        elif len(temp_data) < self.__max_col:
            for index in range(len(temp_data), self.__max_col):
                temp_data.append('')
            self.__data_list[row_index] = [temp for temp in temp_data]
            return True
        else:
            for index in range(self.__max_col, len(temp_data)):
                self.__insert_blank_column()
            self.__data_list[row_index] = [temp for temp in temp_data]
            return True

    # 写列数据，覆盖原来的数据
    def set_column(self, col_index, col_data):
        temp_data = [temp for temp in col_data]
        if self.__file_name == '':
            print('\n请先打开csv文件')
            return False
        elif len(temp_data) == 0:
            print('\n写入数据为空')
            return False
        # 表格为空
        if self.__max_row == 0 and self.__max_col == 0:
            self.__create_blank_cell()
        if col_index >= self.__max_col:
            for index in range(self.__max_col, col_index + 1):
                self.__insert_blank_column()
        if len(temp_data) == self.__max_row:
            for index in range(self.__max_row):
                self.__data_list[index][col_index] = temp_data[index]
            return True
        elif len(temp_data) < self.__max_row:
            for index in range(len(temp_data), self.__max_row):
                temp_data.append('')
            for index in range(self.__max_row):
                self.__data_list[index][col_index] = temp_data[index]
            return True
        else:
            for index in range(self.__max_row, len(temp_data)):
                self.__insert_blank_row()
            for index in range(self.__max_row):
                self.__data_list[index][col_index] = temp_data[index]
            return True

    # 写单元格数据,覆盖原来的数据
    def set_cell(self, row_index, col_index, data):
        if self.__file_name == '':
            print('\n请先打开csv文件')
            return False
        # 表格为空
        if self.__max_row == 0 and self.__max_col == 0:
            self.__create_blank_cell()
        if row_index >= self.__max_row:
            for index in range(self.__max_row, row_index + 1):
                self.__insert_blank_row()
        if col_index >= self.__max_col:
            for index in range(self.__max_col, col_index + 1):
                self.__insert_blank_column()
        self.__data_list[row_index][col_index] = data
        return True

    # 以row_index行为查找源，返回=key的列索引号
    def get_column_index(self, row_index, key):
        row_data = self.get_row(row_index)
        if key not in row_data:
            return -1
        else:
            return row_data.index(key)

    # 以col_index列为查找源，返回=key的行索引号
    def get_row_index(self, col_index, key):
        col_data = self.get_column(col_index)
        if key not in col_data:
            return -1
        else:
            return col_data.index(key)

    # 获取最大行数
    def get_max_row(self):
        return self.__max_row

    # 获取最大列数
    def get_max_column(self):
        return self.__max_col

    # 保存csv文件
    def save(self, file_name=''):
        if file_name != '':
            self.__file_name = file_name
        try:
            with open(self.__file_name, 'w') as outfile:
                for row in self.__data_list:
                    str_write = ''
                    for data in row:
                        str_write += data
                        str_write += ','
                    str_write = str_write[0:len(str_write) - 1]
                    str_write += '\n'
                    outfile.write(str_write)
            return True
        except OSError:
            print(self.__file_name, '文件打开失败')
            return False

    # 打印csv
    def print(self):
        for row in self.__data_list:
            str_read = ''
            for data in row:
                str_read += data
                str_read += ','
            str_read = str_read[0:len(str_read) - 1]
            str_read += '\n'
            print(str_read)

# if __name__ == '__main__':
#     mycsv = MyCSV()
#     mycsv.create('qsj.csv')
#     mycsv.print()
#     print(mycsv.get_max_column(), mycsv.get_max_row())
#     print('\n删除')
#     mycsv.insert_row(mycsv.get_max_row(), ['ni', 'hao', 'ma'])
#     mycsv.insert_row(6, ['wo', 'hen', 'hao'])
#     print(mycsv.get_max_column(), mycsv.get_max_row())
#     mycsv.print()
#     mycsv.save()
