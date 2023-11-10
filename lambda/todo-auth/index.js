import {validateToken} from "./services/auth.service.js";
import {createTestAccessToken} from "./utils/redis.util.js";

export const handler = async (event, context) => {
  const accessToken = await createTestAccessToken();
  console.log(accessToken);

  const checkTokenResult = await validateToken(accessToken);
  console.log(checkTokenResult);

  const response = {
    statusCode: 200,
    body: JSON.stringify('Hello from Lambda!'),
  };
  return response;
};
