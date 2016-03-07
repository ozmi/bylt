import React from 'react';
import ReactDOM from 'react-dom';

import TypeDetail from 'app/model/type_detail';

export default class ModuleDetail extends React.Component {
	constructor(props) {
		super(props);
	}
    render() {
        return (
            <div>
                <span>{this.props.module.name}</span>
                <ul>
                    {
                        Object.keys(this.props.module.types).map(typeName =>
                            <li key={typeName}>
                                <span>{typeName} </span>
                                <div>
                                    <TypeDetail type={this.props.module.types[typeName]} />
                                </div>
                            </li>
                        )
                    }
                </ul>
			</div>
		);
	}
};
