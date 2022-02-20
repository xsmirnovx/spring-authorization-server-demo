import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import Greeting from './components/Greeting.js';
import LogInOut from './components/LogInOut.js';

const config = require('./config');

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
           // email: 'test.owner@auto1.com'
            body: {} // this is the body from /user
        };
    }

    render() {
        return (
            <div id='App'>
                <header>
                    <h1>FusionAuth Example: React</h1>
                    <Greeting body={this.state.body}/>
                    <LogInOut body={this.state.body} uri={`http://localhost:${config.serverPort}`}/>
                </header>
            </div>
        );
    }

    componentDidMount() {
        fetch(`http://localhost:${config.serverPort}/user`, {
            credentials: 'include' // fetch won't send cookies unless you set credentials
        })
            .then(response => response.json())
            .then(response => this.setState(
                {
                    body: response
                })
            );
    }
}

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
