import { SSMClient, GetParameterCommand } from '@aws-sdk/client-ssm';

const client = new SSMClient();

export async function getParameterByName(parameterName, isDecrypt) {
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
  const response = await client.send(command);
  console.log(response);
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
