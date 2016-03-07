import $ from 'jquery';
import React from 'react';
import ReactDOM from 'react-dom';

import ModuleTree from 'app/model/module_tree';
import ModuleDetail from 'app/model/module_detail';

class App extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
		    rootModule: null,
		    selectedModule: null
		};
	}
	componentDidMount() {
        this.serverRequest = $.get(this.props.source, (result) => {
            this.setState({
                rootModule: result
            });
        });
    }
    componentWillUnmount() {
        this.serverRequest.abort();
    }
    handleSelect = (module) => {
        this.setState({
            selectedModule: module
        });
    }
    render() {
        return (
			<div>
			    {this.state.rootModule ? <ModuleTree module={this.state.rootModule} handle_select={this.handleSelect}  /> : ""}
			    {this.state.selectedModule ? <ModuleDetail module={this.state.selectedModule} /> : ""}
			</div>
		);
	}
}

ReactDOM.render(
    <App source="lib.json" />,
    document.getElementById('app'));