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
			<div id="wrapper">
                <div id="sidebar-wrapper">
			        {this.state.rootModule ? <ModuleTree module={this.state.rootModule} handle_select={this.handleSelect}  /> : ""}
			    </div>
			    <div id="page-content-wrapper">
                    <div className="page-content">
                        <div className="row">
                            <div className="col-md-12">
                                {this.state.selectedModule ? <ModuleDetail module={this.state.selectedModule} /> : ""}
                            </div>
                        </div>
                    </div>
        	    </div>
			</div>
		);
	}
}

ReactDOM.render(
    <App source="lib.json" />,
    document.getElementById('app'));