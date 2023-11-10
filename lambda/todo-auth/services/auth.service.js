import jwt from 'jsonwebtoken';
import {createClient} from 'redis';

import TokenExpiredError from "jsonwebtoken/lib/TokenExpiredError.js";
import NotBeforeError from "jsonwebtoken/lib/NotBeforeError.js";
import JsonWebTokenError from "jsonwebtoken/lib/JsonWebTokenError.js";

import {getParameterByName} from "../utils/ssm.util.js";

export function getTokenFromRequest(req) {
}

export async function validateToken(token) {
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
    const privateKey = await getParameterByName('TODO_APP_ACCESS_TOKEN_SECRET', false);
    decodedToken = jwt.verify(token, privateKey);
  } catch (err) {
    response.success = false;
    if (err instanceof TokenExpiredError) {
      response.message = 'Unauthorized! Access Token was expired!';
    } else if (err instanceof NotBeforeError) {
      response.message = 'jwt not active';
    } else if (err instanceof JsonWebTokenError) {
      response.message = 'jwt malformed';
    }
    return response;
  }

  // validate by check in redis
  if(!await validateTokenInRedis(decodedToken)) {
    response.success = false;
    response.message = 'Invalid token';
    return response;
  }
  return response;
}

async function validateTokenInRedis(decodedToken) {
  const redisHost = await getParameterByName('TODO_APP_REDIS_HOST', false);
  const redisClient =  createClient({url: 'redis://' + redisHost});
  await redisClient.connect();

  const tokenId = decodedToken.jti;
  const accessToken = await redisClient.get(tokenId);

  // validate exist by token id
  if(!accessToken) return false;

  return true;
}
