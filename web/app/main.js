import $ from 'jquery';
import React from 'react';
import ReactDOM from 'react-dom';

class App extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
		    types: {}
		};
	}
	componentDidMount() {
        this.serverRequest = $.get(this.props.source, (result) => {
            this.setState({
                types: result.types
            });
        });
    }
    componentWillUnmount() {
        this.serverRequest.abort();
    }
    render() {
        return (
			<div>
			    <dl>
			        {
			            Object.keys(this.state.types).map(
			                (type) => {
			                    return [
			                        <dt key={type + '.dt'}>{type}</dt>,
			                        <dd key={type + 'dd'}>{JSON.stringify(this.state.types[type])}</dd>
			                    ];
			                }
			            )
			        }
			    </dl>
			</div>
		);
	}
}

ReactDOM.render(
    <App source="lib.json" />,
    document.getElementById('app'));