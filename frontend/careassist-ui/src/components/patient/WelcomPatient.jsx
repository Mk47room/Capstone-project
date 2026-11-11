function WelcomPatient (){
    
    return(
        <div>
            <h5>Welcome back, {localStorage.getItem('username')}</h5>
        </div>
    )
}
export default WelcomPatient