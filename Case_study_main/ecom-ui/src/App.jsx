import { Route, Routes } from "react-router";
import ObjectDashboard from "./components/ObjectDashboard";
import ObjectAdd from "./components/ObjectAdd";


function App() {

  return (
   <Routes>
    <Route path="" element={<ObjectDashboard />} />
    <Route path="/add"element = {<ObjectAdd />} />
   </Routes>
  )
}

export default App
