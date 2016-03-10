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
                <h2>{this.props.module.name}</h2>
                    {
                        Object.keys(this.props.module.types).map(typeName =>
                            <div key={typeName} className="panel panel-default">
                                <div className="panel-heading">{typeName}</div>
                                <div className="panel-body">
                                    <TypeDetail type={this.props.module.types[typeName]} />
                                </div>
                            </div>
                        )
                    }
			</div>
		);
	}
};
