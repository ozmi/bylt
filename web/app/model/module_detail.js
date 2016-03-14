import React from 'react';
import ReactDOM from 'react-dom';

import TypeDeclDetail from 'app/model/type_decl_detail';

export default class ModuleDetail extends React.Component {
	constructor(props) {
		super(props);
	}
    render() {
        return (
            <div>
                {
                    Object.keys(this.props.module.types).map(typeName =>
                        <TypeDeclDetail
                            key={typeName}
                            typeName={typeName}
                            type={this.props.module.types[typeName]}
                        />
                    )
                }
			</div>
		);
	}
};
