# Echo Network

## Project Overview

This is an online blog application and content management system designed to revive in-depth content creation and exchange. It includes both frontend and backend code. The frontend is built using the Vue.js framework, while the backend is implemented in Java and Kotlin, based on Servlet and JDBC



## Project Demonstration

Please check the `docs/Project Demonstration` files



## Project Structure

- `echo-network-backend`: Backend code, including controllers, services, data access objects (DAO), entity classes, etc
- `echo-network-vue`: Frontend code based on Vue.js, including components, routing, styles, etc
- `echo-network-react`: Frontend code based on React (Not implemented)



## Functional Modules

### Backend Functional Modules

- **Article Management**: Includes operations such as creating, updating, deleting, liking, bookmarking, and commenting on articles
- **User Management**: Includes user registration, login, personal information management, following/unfollowing, etc
- **Community Management**: Includes creating, joining, leaving, and updating communities
- **Comment Management**: Includes creating, deleting, and liking comments
- **Tag Management**: Includes creating and querying tags
- **Permission Management**: Role-based access control, such as regular users, administrators, etc

### Frontend Functional Modules

- **Homepage**: Displays recommended articles, popular articles, etc
- **Article Details Page**: Shows article content, comments, likes, bookmarks, etc
- **User Center**: Displays user personal information, following/follower lists, published articles, etc
- **Community Page**: Shows community information, members, articles, etc
- **Search Page**: Supports searching for articles, users, and communities



## Technology Stack

- **Backend**: Java, Servlet, JDBC, MySQL, Redis, JWT
- **Frontend**: Vue.js, React, TypeScript, HTML, CSS



## Installation and Execution

### Backend Installation and Execution

1. Install the Java and Kotlin environments
2. Install the MySQL database and create the corresponding database and tables (refer to the `docs` directory)
3. Install Redis and start the service
4. Import the `echo-network-backend` project into an IDE (e.g., IntelliJ IDEA)
5. Configure database connection information, Redis connection information, etc
6. Run the project and ensure the service starts successfully

### Frontend Installation and Execution

1. Install Node.js and npm
2. Navigate to the `echo-network-vue` directory
3. Run `npm install` to install dependencies
4. Run `npm start` to start the development server
5. Open a browser and visit `http://localhost:8080` (Vue) to view the application



## Documentation and Resources

- Project Documentation: The `docs` directory contains relevant design documents and screenshots for the project
- Configuration Files: `application.yml` contains the project's configuration information
- Log Files: `log4j.properties` configures log output



## License

This project is licensed under the MIT License. For more details, please see the `LICENSE` file



## Contribution

Contributions to the code and documentation are welcome. Please submit PRs or Issues



> ***Elegance is not a dispensable luxury but a quality that decides between success and failure!***
> **优雅不是可有可无的奢侈品，而是决定成败的品质！**