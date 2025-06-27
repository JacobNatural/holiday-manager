import React from "react";

const Button =({onClick, children, id}) => {
    return <button type={"button"} {...(id && {id})} className={"standard-button"} onClick={onClick}>{children}</button>;
};

export default Button;