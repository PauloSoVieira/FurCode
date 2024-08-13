<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a id="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
<div align="center">
  
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![Pull Requests][pull-requests-shield]][pull-requests-url]
[![MIT License][license-shield]][license-url]

</div>
<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/PauloSoVieira/FurCode">
    <img src="https://openmoji.org/data/color/svg/1F439.svg" alt="Logo" width="80" height="80">
  </a>
<h3 align="center">FurCode</h3>

  <p align="center">
    Open Source Animal Shelter Manager
    <br />
    <a href="https://github.com/PauloSoVieira/FurCode"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/PauloSoVieira/FurCode">View Demo</a>
    ·
    <a href="https://github.com/PauloSoVieira/FurCode/issues/new?labels=bug&template=bug-report---.md">Report Bug</a>
    ·
    <a href="https://github.com/PauloSoVieira/FurCode/issues/new?labels=enhancement&template=feature-request---.md">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

FurCode is a manager for your local animal shelter.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

* [![Java][java]][java-url]
* [![Spring Boot][spring-boot]][spring-boot-url]
* [![PostgreSQL][postgresql]][postgresql-url]
* [![MinIO][minio]][minio-url]
* [![Redis][redis]][redis-url]
* [![Docker Compose][docker-compose]][docker-compose-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

* Java 17
* Maven
* Docker and Docker Compose
* Git

#### MacOS with brew
  ```sh
  brew install openjdk@17
  brew install maven
  brew install --cask docker
  brew install git
  ```

### Run the project

1. Clone the repo
   
   ```sh
   git clone https://github.com/PauloSoVieira/FurCode.git
   ```
3. Run it
   
   ```sh
   mvn spring-boot:run
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

Go to ```http://localhost:8080/swagger-ui/index.html``` and explore the API.

_For more examples, please refer to the [Documentation](https://github.com/PauloSoVieira/FurCode)_

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- ROADMAP -->
## Roadmap

- [ ] Shelter Personalisation
- [ ] Payment system
- [ ] Quiz to match person tastes with available animals
- [ ] Implement a message broker

See the [open issues](https://github.com/PauloSoVieira/FurCode/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Contributors:

<a href="https://github.com/PauloSoVieira/FurCode/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=PauloSoVieira/FurCode" alt="contrib.rocks image" />
</a>



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

Project Link: [https://github.com/PauloSoVieira/FurCode](https://github.com/PauloSoVieira/FurCode)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* [Mindera](https://mindera.com/)


<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/PauloSoVieira/FurCode.svg?style=for-the-badge
[contributors-url]: https://github.com/PauloSoVieira/FurCode/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/PauloSoVieira/FurCode.svg?style=for-the-badge
[forks-url]: https://github.com/PauloSoVieira/FurCode/network/members
[stars-shield]: https://img.shields.io/github/stars/PauloSoVieira/FurCode.svg?style=for-the-badge
[stars-url]: https://github.com/PauloSoVieira/FurCode/stargazers
[issues-shield]: https://img.shields.io/github/issues/PauloSoVieira/FurCode.svg?style=for-the-badge
[issues-url]: https://github.com/PauloSoVieira/FurCode/issues
[pull-requests-shield]: https://img.shields.io/github/issues-pr/PauloSoVieira/FurCode.svg?style=for-the-badge
[pull-requests-url]: https://github.com/PauloSoVieira/FurCode/pulls
[license-shield]: https://img.shields.io/github/license/PauloSoVieira/FurCode.svg?style=for-the-badge
[license-url]: https://github.com/PauloSoVieira/FurCode/blob/master/LICENSE.txt

[spring-boot]: https://img.shields.io/badge/Spring%20Boot-3.3.2-brightgreen?logo=spring-boot
[spring-boot-url]: https://spring.io/projects/spring-boot
[java]: https://img.shields.io/badge/Java-17-orange?logo=java&logoColor=white
[java-url]: https://www.java.com/en/
[postgresql]: https://img.shields.io/badge/PostgreSQL-16.3-blue?logo=postgresql&logoColor=white
[postgresql-url]: https://www.postgresql.org/
[minio]: https://img.shields.io/badge/MinIO-2024-red?logo=minio&logoColor=white
[minio-url]: https://min.io/
[redis]: https://img.shields.io/badge/Redis-latest-red?logo=redis&logoColor=white
[redis-url]: https://redis.io/
[docker-compose]: https://img.shields.io/badge/Docker--Compose-2496ED?logo=docker&logoColor=white
[docker-compose-url]: https://docs.docker.com/compose/
