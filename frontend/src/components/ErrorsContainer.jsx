const ErrorsContainer = ({display, children}) => {
    return <div id="errors" role="alert" aria-live="assertive" style={{display: display}}>{children}</div>
}

export default ErrorsContainer;