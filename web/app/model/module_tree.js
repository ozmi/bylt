import React from 'react';
import ReactDOM from 'react-dom';

import ListItem from 'material-ui/lib/lists/list-item';

export default class ModuleTree extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
		    expanded: true
		};
	}

    handleSelect = () => {
        this.props.handle_select(this.props.module);
    }

    handleArrowClick = () => {
        this.setState ({
            expanded: ! this.state.expanded
        });
    }

    render() {
        var indentStyle = {
            paddingLeft: (10 * this.props.depth) + "px"
        };
        var listItemClassNames = "list-item";
        if (this.props.selectedModule == this.props.module) {
            listItemClassNames += " selected";
        }
        var nestedModuleStyle = {
            display: this.state.expanded ? "block" : "none"
        };
        return (
            <div>
                <div
                    className={ listItemClassNames }
                    onClick={ this.handleSelect }>
                    <div
                        className="list-item-label"
                        style={ indentStyle }>
                        <span
                            className="list-item-arrow"
                            onClick={this.handleArrowClick}>
                            {
                                Object.keys (this.props.module.modules).length > 0 ? (
                                    this.state.expanded ? (
                                        "\u25E2"
                                    ) : (
                                        "\u25B7"
                                    )
                                ) : (
                                    "\u00A0"
                                )
                            }
                        </span>
                        <span className="list-item-arrow"> </span>
                        <span>
                            { this.props.module.name.split('_').join(' ') }
                        </span>
                    </div>
                </div>
                <div style={nestedModuleStyle}>
                {
                    Object.values (this.props.module.modules).map ((module) =>
                        <ModuleTree
                            key={ module.name }
                            depth={ this.props.depth + 1 }
                            module={ module}
                            handle_select={ this.props.handle_select }
                            selectedModule={ this.props.selectedModule } />
                    )
                }
                </div>
            </div>
        );
	}
};
