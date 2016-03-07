import React from 'react';
import ReactDOM from 'react-dom';

class LambdaType extends React.Component {
	constructor(props) {
		super(props);
	}
    render() {
        const arg = this.props.args[0];
        const ret = this.props.args[1];
        return (
            <span><TypeDetail type={arg} /> => <TypeDetail type={ret} /></span>
		);
	}
}

class ManyType extends React.Component {
	constructor(props) {
		super(props);
	}
    render() {
        const elem = this.props.args[0];
        const sequential = this.props.args[1];
        const unique = this.props.args[2];
        return (
            <span>many <TypeDetail type={elem} /></span>
		);
	}
}

class RecordType extends React.Component {
	constructor(props) {
		super(props);
	}
    render() {
        const fields = this.props.args[0];
        return (
            <div>
                {"{"}
                <table>
                <tbody>
                {
                    fields.map(entry => {
                        const [name, value] = entry;
                        return (
                            <tr key={name}>
                                <td>{name}</td>
                                <td>:</td>
                                <td><TypeDetail type={value} /></td>
                            </tr>
                        );
                    })
                }
                </tbody>
                </table>
                {"}"}
            </div>
		);
	}
}

class TaggedUnionType extends React.Component {
	constructor(props) {
		super(props);
	}
    render() {
        const cases = this.props.args[0];
        return (
            <div>
                <table>
                <tbody>
                {
                    Object.keys(cases).map(tag => {
                        return (
                            <tr key={tag}>
                                <td>{tag}</td>
                                <td>:</td>
                                <td><TypeDetail type={cases[tag]} /></td>
                            </tr>
                        );
                    })
                }
                </tbody>
                </table>
            </div>
		);
	}
}

class TupleType extends React.Component {
	constructor(props) {
		super(props);
	}
    render() {
        const elems = this.props.args[0];
        return (
            <span>({
                elems.map((elem, index) => {
                    return (
                        <span key={index}>
                            {index == 0 ? '' : ', '}
                            <TypeDetail type={elem} />
                        </span>
                    );
                })
            })</span>
		);
	}
}

class TypeRef extends React.Component {
	constructor(props) {
		super(props);
	}
    render() {
        const qname = this.props.args[0];
        const parts = qname.split('/');
        const name = parts[parts.length-1];
        return (
            <span>{name}</span>
		);
	}
}

class UnitType extends React.Component {
	constructor(props) {
		super(props);
	}
    render() {
        return (
            <span>{this.props.args[0]}</span>
		);
	}
}

export default class TypeDetail extends React.Component {
	constructor(props) {
		super(props);
	}
    render() {
        const typeTag = Object.keys(this.props.type)[0];
        const args = Object.values(this.props.type)[0];
        switch (typeTag) {
            case "LambdaType":
                return <LambdaType args={args} />;
            case "ManyType":
                return <ManyType args={args} />;
            case "RecordType":
                return <RecordType args={args} />;
            case "TaggedUnionType":
                return <TaggedUnionType args={args} />;
            case "TupleType":
                return <TupleType args={args} />;
            case "TypeRef":
                return <TypeRef args={args} />;
            case "UnitType":
                return <UnitType args={args} />;
            default:
                return (
                    <div>
                        <span>{typeTag}</span>
                        <span>{JSON.stringify(args)}</span>
                    </div>
                );
        }
	}
};
