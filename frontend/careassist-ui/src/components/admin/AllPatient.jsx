import axios from "axios"
import { useEffect, useState } from "react"
import { API_SERVER_URL_DEV } from "../../../config"

function AllPatient() {

    const [page, setPage] = useState(0)
    const [size, setSize] = useState(5)
    const [totalPages, setTotalPages] = useState(0)
    const [totalRecords, setTotalRecords] = useState(0)
    const [currentPageSize, setCurrentPageSize] = useState(0)

    const [errMsg, setErrMsg] = useState("")
    const [errComment, setErrComment] = useState("")
    const [list, setList] = useState([])
    const [searchId, setSearchId] = useState("")
    const [name, setName] = useState("")
    const [dob, setDob] = useState("")
    const [contact, setContact] = useState("")
    const [riskCategory, setRiskCategory] = useState("")
    const [createdAt, setCreatedAt] = useState("")
    const [isActive, setIsActive] = useState("")

    const [showModal, setShowModal] = useState(false)

    useEffect(() => {
        const getAllPatients = async () => {
            try {
                const response = await axios.get(`${API_SERVER_URL_DEV}/api/patient/all?page=${page}&size=${size}`, {
                    headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
                })

                setList(response.data.list)
                setTotalPages(response.data.paginationInfo.totalPages)
                setTotalRecords(response.data.paginationInfo.totalRecords)
                setCurrentPageSize(response.data.paginationInfo.numRecordsInPage)

            } catch (err) {
                setErrMsg(err?.response?.data?.message)
                setErrComment(err?.response?.data?.comment)
            }
        }

        getAllPatients()
    }, [page, size])

    const onDeactivate = async (id) => {
        try {
            await axios.put(`${API_SERVER_URL_DEV}/api/patient/deactive/${id}`,{},{
                headers : {Authorization : 'Bearer ' +localStorage.getItem('token')}
            })
            alert("Patient Deactivated!!")
        } catch (err) {
            setErrMsg(err?.response?.data?.message)
            setErrComment(err?.response?.data?.comment)
        }

        setShowModal(false)
    }

    const onPageChange = (val) => {
        setPage(val)
    }

    const onSearch = async (e) => {
        e.preventDefault()
        try {
            setErrMsg("")
            setErrComment("")
            const response = await axios.get(`${API_SERVER_URL_DEV}/api/patient/${searchId}`, {
                headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
            })
            setName(response.data.name)
            setContact(response.data.contact)
            setDob(response.data.dob)
            setRiskCategory(response.data.riskCategory)
            setIsActive(response.data.isActive)
            setCreatedAt(response.data.createdAt)
            console.log(isActive)
            setShowModal(true)

        } catch (err) {
            setErrMsg(err?.response?.data?.message || "Patient not found")
            setErrComment(err?.response?.data?.comment || "Please check the Patient ID.")
        }
    }

    return (
        <div>
            <div className="card border-0 shadow-sm">
                <div className="card-header bg-white d-flex justify-content-between align-items-center py-3">
                    <h5 className="mb-0">All Patients</h5>
                    <form className="d-flex" style={{ width: "300px" }}>
                        <div className="input-group">
                            <input type="number" className="form-control form-control-sm" placeholder="Search by ID..."
                                value={searchId} onChange={(e) => setSearchId(e.target.value)}
                            />
                            <button className="btn btn-outline-primary btn-sm" type="submit" onClick={(e) => onSearch(e)}> Search </button>
                        </div>
                    </form>
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
                                <th>Patient ID</th>
                                <th>Name</th>
                                <th>DOB</th>
                                <th>Contact</th>
                                <th>Risk Category</th>
                                <th>Created At</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {list?.map((p, index) => (
                                <tr key={index}>
                                    <th>{index + 1}</th>
                                    <td className="fw-semibold">{p.id}</td>
                                    <td>{p.name}</td>
                                    <td>{p.dob}</td>
                                    <td>{p.contact}</td>
                                    <td>{p.riskCategory}</td>
                                    <td>{p.createdAt?.split("T")[0]}</td>
                                    <td>
                                        <button className="btn btn-danger" onClick={() => onDeactivate(p.id)}> Deactivate</button>
                                    </td>
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
            {showModal && (
                <div className="modal show fade d-block" tabIndex="-1" style={{ backgroundColor: "rgba(0,0,0,0.5)" }}>
                    <div className="modal-dialog modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Patient Details (ID: {searchId})</h5>
                                <button type="button" className="btn-close" onClick={() => setShowModal(false)}></button>
                            </div>
                            <div className="modal-body">
                                <p><strong>Name:</strong> {name}</p>
                                <p><strong>DOB:</strong> {dob}</p>
                                <p><strong>Contact:</strong> {contact}</p>
                                <p><strong>Risk Category:</strong> {riskCategory}</p>
                                <p><strong>Active Status :</strong> {isActive ? "Active" : "Deactive"}</p>
                                <p><strong>Created At:</strong> {createdAt?.split("T")[0]}</p>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Close</button>
                                <button type="button" className="btn btn-danger" onClick={() => onDeactivate(searchId)} >Deactivate</button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}

export default AllPatient