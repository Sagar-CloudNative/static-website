# Static Website Project

This repository contains a simple static website designed for learning and practicing Jenkins CI/CD pipelines. The website is built using HTML and CSS and can be deployed on a Dockerized Nginx server.

---

## Project Features

- **Static Pages**:
  - Homepage with an About and Contact section.
- **Responsive Design**:
  - Basic CSS for a clean and simple layout.

---

## File Structure

static-website/ 
    ├── index.html # Main HTML file 
    ├── styles.css # Stylesheet for the website 
    └── README.md # Project documentation


---

## How to Use

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/static-website.git
cd static-website
```

2. Run Locally
Open index.html in your browser to view the website locally.

## Deployment
# Using Docker and Nginx
1. Run an Nginx Container:

```bash
docker run -d --name nginx-server -p 8080:80 nginx
```
2. Copy Files to Nginx Server:

```bash
docker cp ./index.html nginx-server:/usr/share/nginx/html/
docker cp ./styles.css nginx-server:/usr/share/nginx/html/
```
3. Access the Website:

Open http://localhost:8080 in your browser.