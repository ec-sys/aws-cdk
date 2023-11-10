import jwt from 'jsonwebtoken';
import {createClient} from 'redis';
import {SSMClient, GetParameterCommand} from '@aws-sdk/client-ssm';

const ssmClient = new SSMClient();

export const handler = async (event, context) => {
  const privateKey = await getParameterByName('TODO_APP_ACCESS_TOKEN_SECRET', false);
  const redisHost = await getParameterByName('TODO_APP_REDIS_HOST', false);
  console.log(privateKey);
  console.log(redisHost);

  const accessToken = await createTestAccessToken(redisHost, privateKey);
  console.log(accessToken);
  const checkTokenResult = await validateToken(accessToken, redisHost, privateKey);
  console.log(checkTokenResult);

  const response = {
    statusCode: 200,
    body: JSON.stringify('Hello from Lambda!'),
  };
  return response;
};

async function validateToken(token, redisHost, privateKey) {
  let response = {
    success: true,
    message: ''
  }

  // null or empty token
  if (!token) {
    response.success = false;
    response.message = 'Empty token';
    return response;
  }

  // validate by jwt
  let decodedToken;
  try {
    decodedToken = jwt.verify(token, privateKey);
  } catch (err) {
    response.success = false;
    console.log(err);
    return response;
  }

  // validate by check in redis
  if(!await validateTokenInRedis(decodedToken, redisHost)) {
    response.success = false;
    response.message = 'Invalid token';
    return response;
  }
  return response;
}

async function validateTokenInRedis(decodedToken, redisHost) {
  const redisClient = createClient({url: 'redis://' + redisHost});
  await redisClient.connect();

  const tokenId = decodedToken.jti;
  const accessToken = await redisClient.get(tokenId);

  // validate exist by token id
  if(!accessToken) return false;

  return true;
}

async function getParameterByName(parameterName, isDecrypt) {
  const profile = process.env.PROFILE;
  // locally evn
  if(!profile || profile == 'local') {
    return getParameterInEnvLocal(parameterName);
  }

  // lambda evn
  const params = {
    Name: parameterName,
    WithDecryption: isDecrypt
  };
  const command = new GetParameterCommand(params);
  const response = await ssmClient.send(command);
  return response.Parameter.Value;
}

function getParameterInEnvLocal(parameterName) {
  if(parameterName == 'TODO_APP_ACCESS_TOKEN_SECRET') {
    return 'drjoy1115';
  }
  if(parameterName == 'TODO_APP_REDIS_HOST') {
    return 'localhost:6379';
  }
  throw 'Invalid parameter name: ' + parameterName;
}

async function createTestAccessToken(redisHost, privateKey) {
  const tokenId = '133b2bff-e32f-4a5b-9ba4-f84c40ccef96';
  const payload = {
    userInfo: {
      id: 1,
      name: 'longdd90',
      email: 'long.dang@drjoy.vn'
    },
    jti: tokenId
  }

  const accessToken = jwt.sign(payload, privateKey, {
    algorithm: 'HS256',
    expiresIn: '1h'
  });

  const redisClient =  createClient({url: 'redis://' + redisHost});
  await redisClient.connect();

  await redisClient.set(tokenId, accessToken);
  return accessToken;
}
