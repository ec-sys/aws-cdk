import axios from 'axios';
import { createClient } from 'redis';
import mysql from 'mysql2/promise';

import { SSMClient, GetParameterCommand } from "@aws-sdk/client-ssm"; // ES Modules import

export const handler = async (event) => {
  /*
  try {
    const redisOptions = {
      host: 'todo-app-cluster.hcevyb.clustercfg.use1.cache.amazonaws.com',
      port: 6379
    }

    const client =  createClient({url: 'redis://todo-app-cluster.hcevyb.clustercfg.use1.cache.amazonaws.com:6379'});
    client.on('error', err => console.log('Redis Client Error', err));
    client.on('connect', function(result) {
      console.log('connected');
    });
    await client.connect();

    console.log('connect redis ok');
  } catch (error) {
    console.log('error redis');
    console.log(error);
  }

  try {
	  const res = await axios.get(`https://www.boredapi.com/api/activity`);
	  console.log(res.data);
  } catch (error) {
    console.log('error axios');
    console.log(error);
  }
  */
  try {
    /*
    const conn = await mysql.createConnection({
      host: 'todo-app-db.cjcam4zvgp9s.us-east-1.rds.amazonaws.com',
      user: 'admin',
      password: 'root12345',
      database: 'todo_db'
    });
    console.log("Connected!");
    */
    /*
    getParameterFromSystemManager(function() {
      console.log('done');
      context.done(null, 'Hello from Lambda');
    });
    */
    await getParameter("TODO_APP_MYSQL_HOST");
  } catch (error) {
    console.log('error axios');
    console.log(error);
  }
  // TODO implement
  const response = {
    statusCode: 200,
    body: JSON.stringify('Hello from Lambda!'),
  };
  return response;
};
const credentials = {
  accessKeyId: 'AKIA3NQ4ZZQE4PYSADBX',
  secretAccessKey: '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'
}

const config = {
  region: 'us-east-1',
  credentials,
}
const client = new SSMClient(config);

// function getParameterFromSystemManager(callback) {
//     const client = new SSMClient({
//       region: "us-east-1",
//     });

//     var params = {
//         Name: 'TODO_APP_MYSQL_HOST',
//         WithDecryption: false
//     };

//     console.log('in the getParameterFromSystemManager function');
//     const request = new GetParameterRequest(params);
//     const response = await client.send(request);
//     console.log(response);

//     // var request = client.getParameter(params, function(err, data) {
//     //     if (err) console.log(err, err.stack); // an error occurred
//     //     else console.log(data); // successful response

//     //     callback();
//     // });
// }

async function getParameter(parameterName) {
  console.log(parameterName);
  const params = {
    Name: parameterName,
    WithDecryption: false
  };
  const command = new GetParameterCommand(params);
  const response = await client.send(command);
  console.log('data');
  console.log(response);
}
