import {handler} from "./index.js";

const event = {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mbyI6eyJpZCI6MSwibmFtZSI6ImxvbmdkZDkwIiwiZW1haWwiOiJsb25nLmRhbmdAZHJqb3kudm4ifSwianRpIjoiMTMzYjJiZmYtZTMyZi00YTViLTliYTQtZjg0YzQwY2NlZjk2IiwiaWF0IjoxNjk5NjA0ODkxLCJleHAiOjE2OTk2MDg0OTF9.Uf7_SxDWncKYQKxlc3nXcWAZRCRnASteEjsPFjeAKUE"
}


let response = await handler(event);
console.log(response);
