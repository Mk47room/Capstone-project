import PatientClaimChart from "./PatientClaimChart"
import PatientStat from "./PatientStat"
import WelcomPatient from "./WelcomPatient"

function PatientHome() {

    return (

        <div>
            <div>
                <WelcomPatient />
            </div>
            <div>
                <PatientStat />
            </div>
            <div>
                <PatientClaimChart />
            </div>
        </div>
    )
}

export default PatientHome
