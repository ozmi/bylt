import React from 'react';
import ReactDOM from 'react-dom';


export default class ModuleTree extends React.Component {
	constructor(props) {
		super(props);
	}
    render() {
        return (
            <div>
                <span onClick={this.props.handle_select.bind(null, this.props.module)} >{this.props.module.name}</span>
                <ul>
                    {
                        Object.values(this.props.module.modules).map(module =>
                            <li key={module.name}>
                                <ModuleTree module={module} handle_select={this.props.handle_select} />
                            </li>
                        )
                    }
                </ul>
			</div>
		);
	}
};
