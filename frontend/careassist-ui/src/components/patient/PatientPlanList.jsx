import axios from "axios"
import { use, useEffect, useState } from "react"
import { API_SERVER_URL_DEV } from "../../../config"

function PatientPlanList() {

    const [page, setPage] = useState(0)
    const [size, setSize] = useState(5)
    const [totalPages, setTotalPages] = useState(0)
    const [totalRecords, setTotalRecords] = useState(0)
    const [currentPageSize, setCurrentPageSize] = useState(0)

    const [errMsg, setErrMsg] = useState("")
    const [errComment, setErrComment] = useState("")

    const [list, setList] = useState([])
    const [basePremium,setBasePremium] = useState("")
    const [coverageAmount,setCoverageAmount] = useState("")
    const [age,setAge] = useState("")

    useEffect(() => {
        const getPlans = async () => {

            try {

                const response = await axios.get(`${API_SERVER_URL_DEV}/api/insuranceplan/all?basePremium=${basePremium}&coverageAmount=${coverageAmount}&age=${age}&page=${page}&size=${size}`, {
                    headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
                })
                setList(response.data.list)
                setTotalPages(response.data.paginationInfo.totalPages)
                setTotalRecords(response.data.paginationInfo.totalRecords)
                setCurrentPageSize(response.data.paginationInfo.numRecordsInPage)

                // console.log(response.data)
            } catch (err) {
                setErrMsg(err?.response?.data?.message)
                setErrComment(err?.response?.data?.comment)
                // console.log(err)
            }

        }

        getPlans()
    }, [basePremium,coverageAmount,age,page,size])


    const onPageChange = (val) => {
        setPage(val)
    }

    return (
        <div>
            <div className="card border-0 shadow-sm">

                <div className="card-header bg-white d-flex justify-content-between align-items-center py-3">
                    <h5 className="mb-0">Plan List</h5>
                </div>

                {/* <!-- Error / Header --> */}
                <div className="px-3">
                    <p className="text-danger fw-semibold mb-0">
                        {errMsg}
                    </p>
                    <p className="text-muted small mb-0">
                        {errComment}
                    </p>
                </div>

                <div className="table-responsive">
                    <table className="table table-hover align-middle mb-0">
                        <thead className="table-light">
                            <tr>
                                <th>S.No</th>
                                <th>Plan ID</th>
                                <th>Plan Name</th>
                                <th className="text-end">Base Premium (₹)</th>
                                <th className="text-end">Coverage Amount (₹)</th>
                                <th>Age Min</th>
                                <th>Age Max</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th></th>
                                <th></th>
                                <th></th>
                                <th className="text-end">
                                     <input type="number" placeholder="Minimun Base Premium" onChange={(e)=>setBasePremium(e.target.value)} className="form-control"/>
                                </th>
                                <th className="text-end"> 
                                    <input type="number" placeholder="Minimum Coverage Amount" onChange={(e)=>setCoverageAmount(e.target.value)} className="form-control"/>
                                    </th>
                                <th>
                                     <input type="number" placeholder="Enter Age" onChange={(e)=>setAge(e.target.value)} className="form-control"/>
                                </th>
                            </tr>
                            {list?.map((p, index) => (
                                <tr key={index}>
                                    <th>{index + 1}</th>
                                    <td>{p.planId}</td>
                                    <td>{p.name}</td>
                                    <td className="text-end">{p.basePremium}</td>
                                    <td className="text-end">{p.coverageAmount}</td>
                                    <td>{p.ageMin}</td>
                                    <td>{p.ageMax}</td>
                                </tr>
                            ))}

                        </tbody>
                    </table>
                    <nav aria-label="Page navigation example" style={{ "alignContent": 'center' }}>
                        <ul className="pagination justify-content-center">
                            <li className="page-item"><button className="page-link" onClick={() => setPage(page - 1)}
                                disabled={page === 0}>Previous</button></li>

                            {
                                Array.from({ length: totalPages }, (e, index) => (
                                    <li key={index} className="page-item">
                                        <button className="page-link" onClick={() => onPageChange(index)}>{index + 1}</button></li>
                                ))}

                            <li className="page-item"><button className="page-link" onClick={() => setPage(page + 1)}
                                disabled={page === (totalPages - 1)} >Next</button></li>

                        </ul>
                        <p>{`Page ${page + 1} of ${totalPages} total pages`}</p>
                        <p>{`Showing ${currentPageSize} records from total ${totalRecords} records`}</p>
                    </nav>
                </div>
            </div>
        </div>
    )
}

export default PatientPlanList