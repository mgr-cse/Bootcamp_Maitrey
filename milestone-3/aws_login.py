import sys
import subprocess
import os
import json
from traceback import print_exc

token = sys.argv[1]
command = [
    "aws", "sts", "get-session-token",
    "--duration-seconds=129600",
    "--serial-number=arn:aws:iam::094412661274:mfa/marande",
    f"--token-code={token}"
]

with open('./aws_key.json', 'r') as f:
    aws_key = json.load(f)

home_dir = os.environ['HOME']
config_path = os.path.join(home_dir, '.aws/credentials')
print(config_path)

try:
    with open(config_path, 'w') as f:
        f.write('[default]\n')
        f.write(f'aws_access_key_id = {aws_key["aws_access_key_id"]}\n')
        f.write(f'aws_secret_access_key = {aws_key["aws_secret_access_key"]}\n')
    
    result = subprocess.run(command, stdout=subprocess.PIPE)
    #print(result.stdout)
    temp_key = result.stdout.decode("utf-8")
    print(temp_key)
    temp_key = json.loads(temp_key)
    temp_key = temp_key['Credentials']
    with open(config_path, 'w') as f:
        f.write('[default]\n')
        f.write(f'aws_access_key_id = {temp_key["AccessKeyId"]}\n')
        f.write(f'aws_secret_access_key = {temp_key["SecretAccessKey"]}\n')
        f.write(f'aws_session_token = {temp_key["SessionToken"]}\n')
except Exception:
    
    print('Error occured! removing aws config')
    print_exc(1000)
    os.remove(config_path)