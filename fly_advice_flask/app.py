from flask import Flask, Blueprint, jsonify
from flask_cors import CORS
from flask_restful import Resource, Api

app = Flask(__name__)
app.config['DEBUG'] = False
app.config['TESTING'] = False
CORS(app, resources={r"/api/*": {"origins": "*"}})


class FlightSpiderApi(Resource):
    def get(self):
        returnItem = {
        }
        return jsonify(returnItem)

flight_spider_blueprint = Blueprint("flight", __name__)
flight_api = Api(flight_spider_blueprint)
flight_api.add_resource(FlightSpiderApi, "/api/flight")

app.register_blueprint(flight_spider_blueprint)

@app.route('/')
def hello_world():
    return jsonify('Hello World!')


if __name__ == '__main__':
    app.run()
