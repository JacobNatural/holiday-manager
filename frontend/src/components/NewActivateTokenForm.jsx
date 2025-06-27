import SubmitButton from "./SubmitButton";

const NewActivateTokenForm = ({onSubmit, onDisplay}) => {
    return <form id="email-refresh-token" onSubmit={onSubmit} style={{display: onDisplay}}>
        <label htmlFor="input-refresh-token-email" id="label-refresh-token-email">Email:</label>
        <input type="email" id="input-refresh-token-email" name="input-refresh-token-email" required/>
        <SubmitButton id="get-refresh-token">Get token</SubmitButton>
    </form>
}

export default NewActivateTokenForm;