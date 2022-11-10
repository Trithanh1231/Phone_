const app = angular.module("shopping-cart-app", []);
app.controller("shopping-cart-ctrl", function($scope, $http) {
	$scope.form = {};
	$scope.form2 = {};
	$scope.cart = {
		items: [],

		add(id) {
			var item = this.items.find(item => item.id == id);
			if (item) {
				item.qty++;
				this.saveToLocalStorage();
			} else {
				$http.get(`/rest/products/${id}`).then(resp => {
					resp.data.qty = 1;
					this.items.push(resp.data);
					this.saveToLocalStorage();
				})
			}
		},
		find(id) {
			$http.get(`/rest/products/${id}`).then(resp => {
				$scope.form.image = resp.data.image;
				$scope.form.name = resp.data.name;
			})
		},
		findby(id) {
			$http.get(`/rest/products/${id}`).then(resp => {
				$http.get(`/rest/multiimages/${id}`).then(rest => {
				$scope.form.image = resp.data.image;
				$scope.form.name = resp.data.name;
				$scope.form.price = resp.data.price;
				$scope.form.description = resp.data.description;
				$scope.form.discount.number = resp.data.discount.number;
				})
			})
		},
		add1(id) {
			var item = this.items.find(item => item.id == id);
			if (item) {
				item.qty++;
				this.saveToLocalStorage();
				location.href = "/cart/view";
			} else {
				$http.get(`/rest/products/${id}`).then(resp => {
					resp.data.qty = 1;
					this.items.push(resp.data);
					this.saveToLocalStorage();

				})
			}
		},
		remove(id) {
			var index = this.items.findIndex(item => item.id == id);
			this.items.splice(index, 1);
			this.saveToLocalStorage();
		},


		clear() {
			this.items = []
			this.saveToLocalStorage();
		},
		amt_of(item) { },
		get count() {
			return this.items
				.map(item => item.qty)
				.reduce((total, qty) => total += qty, 0);
		},
		get amount() {
			return this.items
				.map(item => item.qty * item.price)
				.reduce((total, qty) => total += qty, 0);
		},
		get amount() {
			return this.items
				.map(item => (item.qty) * (item.price-(item.price*item.discount.number)))
				.reduce((total, qty) => total += qty, 0);
		},
		saveToLocalStorage() {
			var json = JSON.stringify(angular.copy(this.items));
			localStorage.setItem("cart", json);
		},
		loadFormLocalStorage() {
			var json = localStorage.getItem("cart");
			this.items = json ? JSON.parse(json) : [];
		}
	}
	$scope.cart.loadFormLocalStorage();


	$scope.order = {
		createDate: new Date(),
		address: "",
		user: { username: $("#username").text() },
		get orderDetails() {
			return $scope.cart.items.map(item => {
				return {
					product: { id: item.id },
					price: item.price,
					quantity: item.qty,
					color : item.color,
					image : item.image
				}
			});
		},
		purchase() {
			var order = angular.copy(this);
			$http.post("/rest/orders", order).then(resp => {
				alert("Dat hang thanh cong!");
				$scope.cart.clear();
				location.href = "/order/detail/" + resp.data.id;
			}).catch(error => {
				alert("Dat hang loi")
				console.log(error)
			})
		}
	}
})