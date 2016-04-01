import $ from 'jquery';
import React from 'react';
import ReactDOM from 'react-dom';
import injectTapEventPlugin from 'react-tap-event-plugin';

import AppBar from 'material-ui/lib/app-bar';
import LeftNav from 'material-ui/lib/left-nav';
import TextField from 'material-ui/lib/text-field';

import List from 'material-ui/lib/lists/list';

import ModuleTree from 'app/model/module_tree';
import ModuleDetail from 'app/model/module_detail';

injectTapEventPlugin();

class App extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
		    modules: [],
		    selectedModule: null
		};
	}
	componentDidMount() {
        this.serverRequest = $.get(this.props.source, (result) => {
            this.setState({
                modules: result
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
        var content = null;
        if (this.state.selectedModule) {
            content = (
                <ModuleDetail module={this.state.selectedModule} />
            );
        }
        return (
            <div>
                <div className="header">
                    bylt
                </div>
                <div className="nav">
                    {this.state.modules.map(module => <ModuleTree key={module.name} depth={1} module={module} handle_select={this.handleSelect} selectedModule={this.state.selectedModule} />)}
                </div>
                <div className="editor">
                    {content}
                </div>
			</div>
		);
	}
}

ReactDOM.render(
    <App source="lib.json" />,
    document.getElementById('app'));