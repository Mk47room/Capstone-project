import { configureStore } from "@reduxjs/toolkit";
import ProviderReducer from "./reducers/ProviderReducer";

export const store = configureStore({
    reducer : ProviderReducer
})