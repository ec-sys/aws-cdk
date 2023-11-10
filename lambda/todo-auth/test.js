import {handler} from "./index.js";

const event = {
    "routeKey" : "GET /items"
}


let response = await handler(event);
console.log(response);