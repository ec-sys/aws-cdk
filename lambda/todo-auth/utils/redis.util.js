import jwt from 'jsonwebtoken';
import {createClient} from 'redis';
import {getParameterByName} from "./ssm.util.js";

export async function createTestAccessToken() {
  const tokenId = '133b2bff-e32f-4a5b-9ba4-f84c40ccef96';
  const payload = {
    userInfo: {
      id: 1,
      name: 'longdd90',
      email: 'long.dang@drjoy.vn'
    },
    jti: tokenId
  }

  const privateKey = await getParameterByName('TODO_APP_ACCESS_TOKEN_SECRET', false);
  const accessToken = jwt.sign(payload, privateKey, {
    algorithm: 'HS256',
    expiresIn: '1h'
  });

  const redisHost = await getParameterByName('TODO_APP_REDIS_HOST', false);
  const redisClient =  createClient({url: 'redis://' + redisHost});
  await redisClient.connect();

  await redisClient.set(tokenId, accessToken);
  return accessToken;
}
