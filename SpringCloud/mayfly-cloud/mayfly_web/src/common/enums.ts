interface BaseEnum {
    name: string
    value: any
}

export enum ResultEnum {
    SUCCESS = "200",
    NO_LOGIN = "AUTH002",
    ERROR = "400",
    PARAM_ERROR = "405",
    SERVER_ERROR = "500",
    
}