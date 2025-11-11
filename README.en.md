# Echo Network

This is a social network application project containing both frontend and backend code. The frontend is implemented using both the Vue.js and React frameworks, while the backend is developed in Java using Servlet and JDBC.

## Project Structure

- `echo-network-backend`: Backend code including controllers, services, data access objects (DAOs), entity classes, etc.
- `echo-network-vue`: Frontend code based on Vue.js, including components, routing, styles, etc.
- `echo-network-react`: Frontend code based on React, including components, styles, etc.

## Functional Modules

### Backend Functional Modules

- **Article Management**: Includes operations such as creating, updating, deleting, liking, favoriting, and commenting on articles.
- **User Management**: Includes user registration, login, profile management, following/unfollowing, etc.
- **Community Management**: Includes creating, joining, leaving, and updating communities.
- **Comment Management**: Includes creating, deleting, and liking comments.
- **Tag Management**: Includes creating and querying tags.
- **Permission Management**: Role-based access control, such as regular users and administrators.

### Frontend Functional Modules

- **Homepage**: Displays recommended and trending articles.
- **Article Detail Page**: Displays article content, comments, likes, favorites, etc.
- **User Center**: Displays user profile information, followers/following lists, and published articles.
- **Community Page**: Displays community information, members, and articles.
- **Search Page**: Supports searching for articles, users, and communities.

## Technology Stack

- **Backend**: Java, Servlet, JDBC, MySQL, Redis, JWT
- **Frontend**: Vue.js, React, TypeScript, HTML, CSS

## Installation and Setup

### Backend Installation and Setup

1. Install the Java environment.
2. Install MySQL database and create the required database and tables.
3. Install Redis and start the service.
4. Import the `echo-network-backend` project into an IDE (e.g., IntelliJ IDEA).
5. Configure database connection details, Redis connection settings, etc.
6. Run the project to ensure the server starts successfully.

### Frontend Installation and Setup

1. Install Node.js and npm.
2. Navigate to the `echo-network-vue` or `echo-network-react` directory.
3. Run `npm install` to install dependencies.
4. Run `npm start` to start the development server.
5. Open your browser and visit `http://localhost:3000` (React) or `http://localhost:8080` (Vue) to view the application.

## Documentation and Resources

- Project documentation: The `docs` directory contains relevant project documentation and screenshots.
- Configuration files: `application.yml` contains project configuration settings.
- Log files: `log4j.properties` configures log output.

## License

This project is licensed under the MIT License. For details, see the `LICENSE` file.

## Contributions

Contributions to code and documentation are welcome. Please submit a PR or open an issue.