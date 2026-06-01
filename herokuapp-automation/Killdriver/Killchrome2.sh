echo "Killing the chrome driver instance with PID:"
ps -ef | grep chromedriver | grep -v grep|awk '{print $2}'
ps -ef | grep chromedriver | grep -v grep|awk '{print $2}'| xargs kill -9