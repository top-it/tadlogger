 CREATE TABLE request_log 
(
  request_id        INT PRIMARY KEY auto_increment,
  server_name       VARCHAR(45),
  proxied_address   VARCHAR(24),
  remote_address    VARCHAR(24),
  user_name         VARCHAR(128),
  date_request      TIMESTAMP,
  method            VARCHAR(10),
  uri               VARCHAR(40000),
  protocol          VARCHAR(10),
  status            int,
  LENGTH            INT,
  referer           VARCHAR(40000),
  agent             VARCHAR(512),
  latency           INT,
  dispatchtime      INT
)
/ 

CREATE TABLE cookies_log 
(
  request_id   INT,
  name         VARCHAR(1000),
  value VARCHAR(1000),
  CONSTRAINT request_id_fk FOREIGN KEY (request_id) REFERENCES request_log (request_id)
)
