import React from 'react';
import ReactDOM from 'react-dom';

import Card from 'material-ui/lib/card/card';
import CardActions from 'material-ui/lib/card/card-actions';
import CardHeader from 'material-ui/lib/card/card-header';
import FlatButton from 'material-ui/lib/flat-button';
import CardText from 'material-ui/lib/card/card-text';

import TypeDetail from 'app/model/type_detail';

export default class TypeDeclDetail extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
            expanded: false,
        };
	}
	handleExpandChange = (expanded) => {
        this.setState({expanded: expanded});
    }
    render() {
        const title = this.props.typeName.split('_').join(' ');
        return (
            <Card expanded={this.state.expanded} onExpandChange={this.handleExpandChange}>
                <CardHeader
                    title={title}
                    subtitle={"Short description of " + title}
                    actAsExpander={true}
                    showExpandableButton={true}
                />
                <CardText expandable={true}>
                    <TypeDetail type={this.props.type} />
                </CardText>
            </Card>
		);
	}
};
