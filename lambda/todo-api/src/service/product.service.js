import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import {
  DynamoDBDocumentClient,
  ScanCommand,
  PutCommand,
  GetCommand,
  DeleteCommand,
} from "@aws-sdk/lib-dynamodb";

const client = new DynamoDBClient({});

const dynamo = DynamoDBDocumentClient.from(client);

const tableName = "products";

let body;
let statusCode = 200;
const headers = {
  "Content-Type": "application/json",
};

async function get(event) {
  body = await dynamo.send(
      new GetCommand({
        TableName: tableName,
        Key: {
          id: event.pathParameters.id,
        },
      })
  );
  body = body.Item;

  return {
    statusCode,
    body,
    headers,
  };
}

async function remove(event) {
  await dynamo.send(
      new DeleteCommand({
        TableName: tableName,
        Key: {
          id: event.pathParameters.id,
        },
      })
  );
  body = `Deleted item ${event.pathParameters.id}`;

  return {
    statusCode,
    body,
    headers,
  };
}

module.exports = {
  remove
};
