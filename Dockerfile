From clojure
RUN apt-get update
RUN apt-get install -y curl software-properties-common
RUN curl -sL https://deb.nodesource.com/setup_8.x | bash -
RUN apt-get install -y vim
RUN apt-get install -y nodejs
RUN apt-get install -y awscli
RUN npm install -g serverless
VOLUME /project
WORKDIR /project
CMD sls deploy
