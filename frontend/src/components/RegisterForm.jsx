import SubmitButton from "./SubmitButton";

const RegisterForm = ({onSubmit}) => {
   return <form className={'container'} id={'registration-form'} onSubmit={onSubmit}>
       <h2>Registration Form</h2>

       <label htmlFor="name">Name:</label>
       <input type="text" id="name" name="name" className="gridAreaSpace" required/>

       <label htmlFor="surname">Surname:</label>
       <input type="text" id="surname" name="surname" required/>

       <label htmlFor="email">Email:</label>
       <input type="email" id="email" name="email" required/>

       <label htmlFor="username">Username:</label>
       <input type="text" id="username" name="username" required/>

       <label htmlFor="password">Password:</label>
       <input type="password" id="password" name="password" required/>

       <label htmlFor="age" className="gridAreaName">Age:</label>
       <input type="number" id="age" name="age" required/>

       <SubmitButton>Register</SubmitButton>

   </form>;
}

export default RegisterForm;
