<script setup>
import { useAuthStore } from "@/stores/auth";
import {
  Menu,
  Package,
  Receipt,
  ShoppingBasket,
  ShoppingCart,
  StretchHorizontal,
  UserRound,
} from "lucide-vue-next";
import { RouterLink, useRouter, useRoute } from "vue-router";
import Button from "./Button.vue";
import { ref, watch } from "vue";
import ConfirmModal from "./ConfirmModal.vue";
import { useCartStore } from "@/stores/cart";

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const cartStore = useCartStore();

// Search Bar
const searchKeyword = ref("");

const handleSearch = () => {
  // close sidebar
  showSidebar.value = false

  if (searchKeyword.value.trim()) {
    if (route.name !== "SaleItemGallery") {
      router.push({
        name: "SaleItemGallery",
        query: { search: searchKeyword.value.trim() },
      });
    } else {
      router.replace({
        name: "SaleItemGallery",
        query: { ...route.query, search: searchKeyword.value.trim() },
      });
    }
  } else {
    if (route.name === "SaleItemGallery" && route.query.search) {
      const newQuery = { ...route.query };
      delete newQuery.search;
      router.replace({
        name: "SaleItemGallery",
        query: newQuery,
      });
    }
  }
};
const clearSearch = () => {
  searchKeyword.value = "";
  if (route.name === "SaleItemGallery" && route.query.search) {
    const newQuery = { ...route.query };
    delete newQuery.search;
    router.replace({
      name: "SaleItemGallery",
      query: newQuery,
    });
  }
};

watch(
  () => route.query.search,
  (newSearch) => {
    if (newSearch) {
      searchKeyword.value = newSearch;
    } else {
      searchKeyword.value = "";
    }
  },
  { immediate: true }
);

// Sign out
const showConfirmSignoutDialog = ref(false);

const handleShowSignoutDialog = () => {
  showConfirmSignoutDialog.value = true;
};

const handleCloseSignoutDialog = () => {
  showConfirmSignoutDialog.value = false;
};

const confirmSignout = async () => {
  await authStore.logout();
  showConfirmSignoutDialog.value = false;
  cartStore.clearCart();
  router.push({ name: "SaleItemGallery" });
};

// Side bar
const showSidebar = ref(false);

const openSidebar = () => {
  showSidebar.value = true;
};
</script>

<template>
  <header
    class="sticky top-0 z-50 bg-gradient-to-r from-purple-500 to-rose-500 text-white"
  >
    <nav class="px-8 xl:px-16">
      <div class="container mx-auto py-3 flex items-center justify-between">
        <RouterLink :to="{ name: 'SaleItemGallery' }" class="mr-2">
          <div class="flex items-center gap-2">
            <div class="text-white">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="lucide lucide-smartphone"
              >
                <rect width="14" height="20" x="5" y="2" rx="2" ry="2" />
                <path d="M12 18h.01" />
              </svg>
            </div>
            <h2 class="font-bold text-xl whitespace-nowrap">ITBMS-MS3</h2>
          </div>
        </RouterLink>

        <div class="max-lg:hidden flex items-center gap-4 lg:gap-7">
          <!-- Search bar -->
          <div class="relative">
            <input
              type="text"
              placeholder="Search here..."
              class="input !bg-white !rounded-full text-black"
              v-model="searchKeyword"
              @keyup.enter="handleSearch"
            />
            <button
              v-if="searchKeyword"
              @click="clearSearch"
              class="absolute right-10 top-2 text-gray-400 hover:text-gray-600 cursor-pointer"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="lucide lucide-x"
              >
                <path d="M18 6 6 18" />
                <path d="m6 6 12 12" />
              </svg>
            </button>
            <button
              @click="handleSearch"
              class="absolute right-3 top-2 text-gray-400 hover:text-gray-600 cursor-pointer"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="lucide lucide-search"
              >
                <circle cx="11" cy="11" r="8" />
                <path d="m21 21-4.3-4.3" />
              </svg>
            </button>
          </div>

          <!-- Cart -->
          <RouterLink :to="{ name: 'cart' }" class="itbms-shopnow">
            <button class="relative cursor-pointer">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="25"
                height="25"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="lucide lucide-shopping-cart"
              >
                <circle cx="8" cy="21" r="1" />
                <circle cx="19" cy="21" r="1" />
                <path
                  d="M2.05 2.05h2l2.66 12.42a2 2 0 0 0 2 1.58h9.78a2 2 0 0 0 1.95-1.57l1.65-7.43H5.12"
                />
              </svg>
              <span
                v-if="cartStore.items.length > 0"
                class="cursor-pointer absolute -top-2 -right-2 bg-rose-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center"
              >
                {{ cartStore.totalItems }}
              </span>
            </button>
          </RouterLink>

          <div class="flex gap-4">
            <!-- user profile -->
            <div
              v-if="authStore.user && authStore.accessToken"
              class="flex items-center gap-5"
            >
              <RouterLink :to="{ name: 'order' }">
                <button
                  class="flex cursor-pointer bg-white text-rose-600 px-6 py-2 rounded-full font-medium hover:bg-opacity-90 transition-all transform hover:scale-105"
                >
                  <Receipt />
                  Orders
                </button>
              </RouterLink>
              <RouterLink
                v-if="authStore.user.userType === 'SELLER'"
                :to="{ name: 'SaleItemList' }"
                class="itbms-shopnow"
              >
                <button
                  class="flex whitespace-nowrap gap-1 cursor-pointer bg-white text-rose-600 px-6 py-2 rounded-full font-medium hover:bg-opacity-90 transition-all transform hover:scale-105"
                >
                  <Package />
                  Manage items
                </button>
              </RouterLink>
              <RouterLink :to="{ name: 'userProfile' }">
                <button
                  class="cursor-pointer w-8 h-8 rounded-full bg-gray-200 flex items-center justify-center text-black"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="16"
                    height="16"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    class="lucide lucide-user"
                  >
                    <circle cx="12" cy="8" r="5" />
                    <path d="M20 21a8 8 0 0 0-16 0" />
                  </svg>
                </button>
              </RouterLink>

              <Button
                class-name="ghost-btn whitespace-nowrap"
                :onclick="handleShowSignoutDialog"
              >
                Sign out
              </Button>
            </div>

            <!-- register/sign-in -->
            <div v-else class="flex gap-2">
              <RouterLink :to="{ name: 'login' }">
                <Button class-name="ghost-btn"> Sign in </Button>
              </RouterLink>
              <RouterLink :to="{ name: 'register' }">
                <Button variant="secondary"> Sign up </Button>
              </RouterLink>
            </div>
          </div>
        </div>

        <!-- Mobile Burger -->
        <div class="lg:hidden cursor-pointer" @click="openSidebar">
          <Menu></Menu>
        </div>
      </div>
    </nav>
  </header>

  <!-- Mobile sidebar -->
  <div v-show="showSidebar" class="fixed inset-0 z-90 lg:hidden">
    <!-- Backdrop -->
    <div
      class="absolute inset-0 bg-black/60 transition-opacity"
      @click="showSidebar = false"
      aria-hidden="true"
    ></div>

    <!-- Sliding panel -->
    <aside
      class="absolute right-0 top-0 h-full w-3/4 max-w-xs bg-gradient-to-b from-purple-600 to-rose-500 text-white shadow-lg transform transition-transform duration-300"
      :class="{
        'translate-x-0': showSidebar,
        'translate-x-full': !showSidebar,
      }"
      @click.stop
      role="dialog"
      aria-modal="true"
    >
      <div class="flex items-center justify-between p-4">
        <RouterLink
          :to="{ name: 'SaleItemGallery' }"
          @click="showSidebar = false"
          class="font-semibold flex gap-1"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="lucide lucide-smartphone"
          >
            <rect width="14" height="20" x="5" y="2" rx="2" ry="2" />
            <path d="M12 18h.01" />
          </svg>
          ITBMS-MS3
        </RouterLink>

        <button
          @click="showSidebar = false"
          aria-label="Close sidebar"
          class="p-2 cursor-pointer"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="18"
            height="18"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <path d="M6 6l12 12M6 18L18 6" />
          </svg>
        </button>
      </div>

      <div class="p-4 flex-1 overflow-auto flex flex-col gap-4">
        <!-- Search -->
        <div>
          <div class="relative">
            <input
              type="text"
              v-model="searchKeyword"
              @keyup.enter="handleSearch"
              placeholder="Search..."
              class="w-full px-3 py-2 rounded-full border border-white text-white outline-none"
            />
            <button
              v-if="searchKeyword"
              @click="clearSearch"
              class="absolute right-10 top-2 text-sm cursor-pointer"
            >
              Clear
            </button>
            <button
              @click="handleSearch"
              class="absolute right-3 top-2 text-sm cursor-pointer"
            >
              Go
            </button>
          </div>
        </div>

        <!-- Links -->
        <nav class="flex flex-col gap-2">
          <RouterLink
            :to="{ name: 'SaleItemGallery' }"
            @click="showSidebar = false"
            class="flex gap-3 px-3 py-2 rounded hover:bg-white/10"
          >
            <ShoppingBasket />
            Browse
          </RouterLink>

          <RouterLink
            v-if="authStore.user"
            :to="{ name: 'cart' }"
            @click="showSidebar = false"
            class="flex items-center justify-between px-3 py-2 rounded hover:bg-white/10"
          >
            <span class="flex gap-3">
              <ShoppingCart />
              Cart
            </span>
            <span
              v-if="cartStore.items.length"
              class="bg-rose-600 rounded-full w-6 h-6 text-center text-sm"
              >{{ cartStore.totalItems }}</span
            >
          </RouterLink>

          <RouterLink
            v-if="authStore.user"
            :to="{ name: 'order' }"
            @click="showSidebar = false"
            class="px-3 py-2 rounded hover:bg-white/10 flex gap-3"
          >
            <Receipt />
            Orders
          </RouterLink>

          <RouterLink
            v-if="authStore.user && authStore.user?.userType === 'SELLER'"
            :to="{ name: 'SaleItemList' }"
            @click="showSidebar = false"
            class="flex gap-3 px-3 py-2 rounded hover:bg-white/10"
          >
            <Package />
            Manage Sale Items
          </RouterLink>
        </nav>

        <!-- Auth actions -->
        <div class="mt-auto pt-4 border-t border-white/20">
          <div
            v-if="authStore.user && authStore.accessToken"
            class="flex flex-col gap-2"
          >
            <RouterLink
              :to="{ name: 'userProfile' }"
              @click="showSidebar = false"
              class="px-3 py-2 rounded hover:bg-white/10 flex gap-3"
            >
              <UserRound />
              Profile
            </RouterLink>
            <Button
              class-name="ghost-btn"
              :onclick="
                () => {
                  handleShowSignoutDialog();
                  showSidebar = false;
                }
              "
              >Sign out</Button
            >
          </div>

          <div v-else class="flex flex-col gap-2">
            <RouterLink
              :to="{ name: 'login' }"
              @click="showSidebar = false"
              class="w-full"
            >
              <Button class-name="ghost-btn !w-full">Sign in</Button>
            </RouterLink>
            <RouterLink
              :to="{ name: 'register' }"
              @click="showSidebar = false"
              class="w-full"
            >
              <Button variant="secondary" class-name="!w-full">Sign up</Button>
            </RouterLink>
          </div>
        </div>
      </div>
    </aside>
  </div>

  <ConfirmModal
    v-if="showConfirmSignoutDialog"
    :title="'Sign out Confirmation'"
    :message="`Are you sure you want to sign out?`"
    :button-label="'Sign out'"
    @confirm="confirmSignout"
    @cancel="handleCloseSignoutDialog"
  />
</template>

<style scoped></style>
