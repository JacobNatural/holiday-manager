import React from 'react'

const Div = ({children, id}) =>{
    return <div className={"container"} {...(id && {id})}>{children}</div>
}

export default Div;