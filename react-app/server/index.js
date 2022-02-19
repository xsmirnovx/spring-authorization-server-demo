const express = require('express');
const cors = require('cors');
const config = require('../config');

// configure Express app and install the JSON middleware for parsing JSON bodies
const app = express();
app.use(express.json());

// configure CORS
app.use(cors({
    origin: true,
    credentials: true
}));

// use routes
app.use('/user', require('./routes/user'));
app.use('/login', require('./routes/login'));
app.use('/oauth-callback', require('./routes/oauth_callback'));

// start server
app.listen(config.serverPort,
    () => console.log(`FusionAuth example app listening on port ${config.serverPort}.`));