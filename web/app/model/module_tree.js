import React from 'react';
import ReactDOM from 'react-dom';

import ListItem from 'material-ui/lib/lists/list-item';

export default class ModuleTree extends React.Component {
	constructor(props) {
		super(props);
	}

	renderItem = (module, index) => {
        return (
            <ListItem
                key={index}
                primaryText={module.name}
                secondaryText={"Description of module " + module.name}
                initiallyOpen={true}
                onTouchTap={this.props.handle_select.bind(null, module)}
                nestedItems={
                    Object.values(module.modules).map((module, index) =>
                        this.renderItem(module, index)
                    )
                }
            />
        );
	}

    render() {
        return this.renderItem(this.props.module, 0);
	}
};
