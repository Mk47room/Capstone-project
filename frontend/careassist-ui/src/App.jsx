import { Route, Routes } from "react-router"
import PageNotFound from "./components/PageNotFound"
import Login from "./components/auth/Login"
import SignUp from "./components/patient/SignUp"
import PatientDashboard from "./components/patient/PatientDashboard"
import AdminDashboard from "./components/admin/AdminDashboard"
import ProviderDashboard from "./components/healthcareprovider/ProviderDashboard"
import InsurerDashboard from "./components/insurer/InsurerDashboard"
import ProviderProfile from "./components/healthcareprovider/ProviderProfile"
import ProviderHome from "./components/healthcareprovider/ProviderHome"
import Invoice from "./components/healthcareprovider/Invoice"
import PatientHome from "./components/patient/PatientHome"
import PatientProfile from "./components/patient/PatientProfile"
import PatientPlanList from "./components/patient/PatientPlanList"
import PatientPolicy from "./components/patient/PatientPolicy"
import PatientClaim from "./components/patient/PatientClaim"
import InsurerHome from "./components/insurer/InsurerHome"
import InsurerClaimList from "./components/insurer/InsurerClaimList"
import InsurerPlan from "./components/insurer/InsurerPlan"
import AdminHome from "./components/admin/AdminHome"
import AllPatient from "./components/admin/AllPatient"
import AllProvider from "./components/admin/AllProvider"

function App() {


  return (
    <Routes>
      <Route path="" element={<Login />} />
      <Route path="patient/signup" element={<SignUp />} />

      <Route path="patient/:token" element={<PatientDashboard />}>
        <Route path="" element={<PatientHome />} />
        <Route path="planlist" element={<PatientPlanList />} />
        <Route path="policy" element={<PatientPolicy />} />
        <Route path="claim" element={<PatientClaim />} />
        <Route path="profile" element={<PatientProfile />} />

      </Route>

      <Route path="admin/:token" element={<AdminDashboard />}>
        <Route path="" element={<AdminHome />} />
        <Route path="patient" element={<AllPatient />} />
        <Route path="provider" element={<AllProvider />} />
      </Route>

      <Route path="provider/:token" element={<ProviderDashboard />}>
        <Route path="" element={<ProviderHome />} />
        <Route path="invoice" element={<Invoice />} />
        <Route path="profile" element={<ProviderProfile />} />
      </Route>

      <Route path="insurer/:token" element={<InsurerDashboard />}>
        <Route path="" element={<InsurerHome />} />
        <Route path="plan" element={<InsurerPlan />} />
        <Route path="claim" element={<InsurerClaimList />} />
      </Route>
      <Route path="*" element={<PageNotFound />} />
    </Routes>
  )
}

export default App
