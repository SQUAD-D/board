const apiConfig = {
    development: 'http://localhost:8080',
    production: ''
};

const environment = process.env.NODE_ENV || 'development';

module.exports = {
    apiEndpoint: apiConfig[environment]
};
