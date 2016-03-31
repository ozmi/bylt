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
        var content = null;
        if (this.state.selectedModule) {
            content = (
                <div className="page-content">
                    <ol className="breadcrumb">
                      <li><a href="#">lib</a></li>
                      <li className="active">{this.state.selectedModule.name}</li>
                    </ol>
                    <div className="row">
                        <div className="col-md-12">
                            <ModuleDetail module={this.state.selectedModule} />
                        </div>
                    </div>
                </div>
            );
        }
        return (
            <div>
                <AppBar title="bylt">
                </AppBar>
                <div id="wrapper">
                    <LeftNav open={true}>
                        <AppBar title="bylt">
                        </AppBar>
                        <TextField hintText="search ..." />
                        <List>
                            {this.state.rootModule ? Object.values(this.state.rootModule.modules).map(module => <ModuleTree key={module.name} module={module} handle_select={this.handleSelect}  />) : ""}
                        </List>
                    </LeftNav>
                    <div id="page-content-wrapper">
                        {content}
                    </div>
                </div>
			</div>
		);
	}
}

ReactDOM.render(
    <App source="lib.json" />,
    document.getElementById('app'));