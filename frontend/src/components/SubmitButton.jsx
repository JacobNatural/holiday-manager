import React from "react";

const SubmitButton =({onClick, children, id}) => {
    return <button type={"submit"} {...(id && {id})} className={"standard-button"} onClick={onClick}>{children}</button>;
};

export default SubmitButton;