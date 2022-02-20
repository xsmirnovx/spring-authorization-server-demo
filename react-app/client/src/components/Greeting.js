import React from 'react';

export default class Greeting extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let message = (this.props.body.user)
            ? `Hi, ${this.props.body.user.email}!`
            : "You're not logged in.";

        return (
            <span>{message}</span>
        );
    }
}
