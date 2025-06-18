resource "aws_dynamodb_table" "tasks" {
  name           = "tasks"
  billing_mode   = "PAY_PER_REQUEST"
  hash_key       = "id"

  attribute {
    name = "id"
    type = "S"
  }
}

resource "aws_security_group" "todo_sg" {
  name        = "todo_sg"
  description = "Allow 8080 inbound"

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "todo_server" {
  ami           = "ami-092ff8e60e2d51e19"
  instance_type = "t2.micro"
  security_groups = [aws_security_group.todo_sg.name]
  key_name = var.key_pair_name

  user_data = <<-EOF
              #!/bin/bash
              yum update -y
              amazon-linux-extras install docker -y
              service docker start
              usermod -a -G docker ec2-user
              docker run -d -p 8080:8080 your-dockerhub-username/todo-app
              EOF
}
