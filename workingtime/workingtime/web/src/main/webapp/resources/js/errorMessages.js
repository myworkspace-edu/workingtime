const errorMessages = {
	SPECIAL_CHAR_NOT_ALLOWED: 'Special characters are not allowed in the name.',
	FROM_DATE_REQUIRED: 'From date is required.',
	TO_DATE_REQUIRED: 'To date is required.',
	NAME_REQUIRED: 'Name is required.',
	TO_DATE_AFTER_FROM_DATE: 'To date must be after from date.',
	ALL_CELLS_FILLED: 'All cells must be filled.',
	INVALID_CELL_VALUE: 'Cell value must be "Y" or "N".',
	MUST_REGISTER_NOTE: 'Note is required when any day is marked "N".',
	SPECIAL_CHAR_NOT_ALLOWED: "Special characters are not allowed.",
	NAME_TOO_LONG: "Name cannot exceed 30 characters.",
	NAME_TOO_SHORT: "Name cannot be less than 8 characters."
};


function displayError(message) {
	Swal.fire({
		icon: 'error',
		title: 'Error',
		text: message,
		timer: 2000,
		showConfirmButton: false
	});
}

function displaySuccessMessage(message) {
	Swal.fire({
		icon: 'success',
		title: 'Success',
		text: message,
		timer: 2000,
		showConfirmButton: false
	});
}


//let locale = document.getElementById('currentLocale').value || 'en'; // Get locale from hidden input
//
//const errorMessages = {
////	"vi": {
//		FROM_DATE_REQUIRED: "Từ ngày là bắt buộc.",
//		TO_DATE_REQUIRED: "Đến ngày là bắt buộc.",
//		NAME_REQUIRED: "Tên là bắt buộc.",
//		TO_DATE_AFTER_FROM_DATE: "Đến ngày phải sau từ ngày.",
//		ALL_CELLS_FILLED: "Tất cả các ô phải được điền.",
//		INVALID_CELL_VALUE: "Giá trị ô không hợp lệ. Chỉ cho phép 'Y' hoặc 'N'.",
//		MUST_REGISTER_NOTE: "Bạn phải ghi chú cho các lịch bận.",
//		SPECIAL_CHAR_NOT_ALLOWED: "Không cho phép ký tự đặc biệt.",
//		NAME_TOO_LONG: "Tên không được quá 30 ký tự.",
//		NAME_TOO_SHORT: "Tên không được ít hơn 8 ký tự."
//	},
//	"en": {
//		FROM_DATE_REQUIRED: "From date is required.",
//		TO_DATE_REQUIRED: "To date is required.",
//		NAME_REQUIRED: "Name is required.",
//		TO_DATE_AFTER_FROM_DATE: "To date must be after from date.",
//		ALL_CELLS_FILLED: "All cells must be filled.",
//		INVALID_CELL_VALUE: "Invalid cell value detected. Only 'Y' or 'N' is allowed.",
//		MUST_REGISTER_NOTE: "You must register a note for busy schedules.",
//		SPECIAL_CHAR_NOT_ALLOWED: "Special characters are not allowed.",
//		NAME_TOO_LONG: "Name cannot exceed 30 characters.",
//		NAME_TOO_SHORT: "Name cannot be less than 8 characters."
//	},
//	"ko": {
//		FROM_DATE_REQUIRED: "시작 날짜는 필수입니다.",
//		TO_DATE_REQUIRED: "종료 날짜는 필수입니다.",
//		NAME_REQUIRED: "이름은 필수입니다.",
//		TO_DATE_AFTER_FROM_DATE: "종료 날짜는 시작 날짜 이후여야 합니다.",
//		ALL_CELLS_FILLED: "모든 셀을 채워야 합니다.",
//		INVALID_CELL_VALUE: "잘못된 셀 값이 감지되었습니다. 'Y' 또는 'N'만 허용됩니다.",
//		MUST_REGISTER_NOTE: "바쁜 일정에는 메모를 등록해야 합니다.",
//		SPECIAL_CHAR_NOT_ALLOWED: "특수 문자는 허용되지 않습니다.",
//		NAME_TOO_LONG: "이름은 30자를 초과할 수 없습니다.",
//		NAME_TOO_SHORT: "이름은 8자 이상이어야 합니다."
//	},
//	"ja": {
//		FROM_DATE_REQUIRED: "開始日が必要です。",
//		TO_DATE_REQUIRED: "終了日が必要です。",
//		NAME_REQUIRED: "名前は必須です。",
//		TO_DATE_AFTER_FROM_DATE: "終了日は開始日以降でなければなりません。",
//		ALL_CELLS_FILLED: "すべてのセルに入力する必要があります。",
//		INVALID_CELL_VALUE: "無効なセル値が検出されました。「Y」または「N」のみが許可されます。",
//		MUST_REGISTER_NOTE: "忙しいスケジュールにはメモを登録する必要があります。",
//		SPECIAL_CHAR_NOT_ALLOWED: "特殊文字は許可されていません。",
//		NAME_TOO_LONG: "名前は30文字を超えてはなりません。",
//		NAME_TOO_SHORT: "名前は8文字以上である必要があります。"
//	}
//};
//
//function displayError(message) {
//	Swal.fire({
//		icon: 'error',
//		title: 'Error',
//		text: message,
//		timer: 2000,
//		showConfirmButton: false
//	});
//}
//
//function displaySuccessMessage(message) {
//	Swal.fire({
//		icon: 'success',
//		title: 'Success',
//		text: message,
//		timer: 2000,
//		showConfirmButton: false
//	});
//}
//
//function setLanguage(lang) {
//	locale = lang;
//	document.getElementById('currentLocale').value = locale; // Update hidden input or some storage
//}
//
//
//
//
//
//
//
//
//
