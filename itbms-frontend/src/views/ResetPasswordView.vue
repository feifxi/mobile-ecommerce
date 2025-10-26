<script setup>
import { resetPassword } from "@/api";
import Button from "@/components/Button.vue";
import { useStatusMessageStore } from "@/stores/statusMessage";
import { BadgeCheck, BadgeX, Eye, EyeOff } from "lucide-vue-next";
import { onMounted, reactive, ref, watch } from "vue";
import { RouterLink, useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const { token } = route.query;
if (!token) {
  router.push({ name: "login" });
}

const isLoading = ref(false);
const currentState = ref("RESET_PASSWORD"); // RESET_PASSWORD, SUCCESS, FAILED
const userData = reactive({
  password: "",
  confirmPassword: "",
});

// Form Control
const currentFocusField = ref(null);
const isFormValid = ref(false);
const errorFormMessage = reactive({
  password: "",
  confirmPassword: "",
});

const fieldIntegrity = {
  password: {
    checkConstraint: (data) => {
      if (currentState.value !== "RESET_PASSWORD") return true;
      if (typeof data !== "string") return false;
      const regex =
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,14}$/;
      return regex.test(data);
    },
    errorMessage:
      "Password must be 8-14 characters long and include at least one lowercase letter, one uppercase letter, one number, and one special character.",
  },
  confirmPassword: {
    checkConstraint: (data) => {
      if (currentState.value !== "RESET_PASSWORD") return true;
      return data === userData.password;
    },
    errorMessage: "Passwords do not match.",
  },
};

const handleFocusIn = (e) => {
  currentFocusField.value = e.target.name;
};

const handleFocusOut = (e) => {
  const currentField = e.target.name;
  if (typeof userData[currentField] === "string") {
    userData[currentField] = userData[currentField].trim();
  }
  showErrorToForm();
  currentFocusField.value = null;
};

const checkFieldIntegrity = (field) => {
  if (fieldIntegrity[field]) {
    return fieldIntegrity[field]?.checkConstraint(userData[field]);
  } else {
    return true;
  }
};

const validateAllField = () => {
  let isValid = true;
  for (const field in userData) {
    if (!checkFieldIntegrity(field)) {
      isValid = false;
      break;
    }
  }
  isFormValid.value = isValid;
};

const showErrorToForm = () => {
  const field = currentFocusField.value;
  if (field) {
    // console.log(field)
    errorFormMessage[field] = checkFieldIntegrity(field)
      ? ""
      : fieldIntegrity[field]?.errorMessage;
  }
};

watch(userData, () => {
  showErrorToForm();
  validateAllField();
});

const showPassword = ref(false);
const showConfirmPassword = ref(false);
const togglePassword = () => (showPassword.value = !showPassword.value);
const toggleConfirmPassword = () => (showConfirmPassword.value = !showConfirmPassword.value);

// Send Reset Password
const sendResetPassword = async (e) => {
  e.preventDefault();
  try {
    isLoading.value = true;
    const res = await resetPassword(token, userData.password);
    const data = await res.json();
    console.log(data);
    if (res.ok) {
      currentState.value = "SUCCESS";
    } else throw new Error("Invalid credential.");
  } catch (err) {
    console.log(err);
    currentState.value = "FAILED";
  }
  isLoading.value = false;
};
</script>

<template>
  <main class="px-4 sm:px-16 py-8">
    <div class="bg-white rounded-xl shadow">
      <!-- Loading -->
      <div v-if="isLoading" class="text-center py-16">
        <div
          class="inline-block animate-spin rounded-full h-16 w-16 border-4 border-purple-200 border-t-purple-600 mb-4"
        ></div>
        <p class="text-gray-600 text-lg">Loading...</p>
      </div>

      <!-- Reset Password Form -->
      <div v-else-if="currentState === 'RESET_PASSWORD'">
        <div
          class="px-16 py-32 flex flex-col gap-3 justify-center items-center"
        >
          <h2 class="text-3xl text-center">Reset Your Password</h2>
          <form
            @submit="sendResetPassword"
            class="flex flex-col gap-3 w-full max-w-xl"
          >
            <div class="flex flex-col gap-1">
              <label>
                <span class="text-red-500 text-xl">*</span>
                Password
              </label>
              <div class="relative">
                <input
                  name="password"
                  :type="showPassword ? 'text' : 'password'"
                  class="input w-full"
                  placeholder="Password..."
                  v-model="userData.password"
                  @focusin="handleFocusIn"
                  @focusout="handleFocusOut"
                />
                <button
                  type="button"
                  @click="togglePassword"
                  class="absolute right-2 top-2 text-gray-500 cursor-pointer"
                >
                  <component
                    :is="showPassword ? EyeOff : Eye"
                    class="w-5 h-5"
                  />
                </button>
              </div>
              <p class="itbms-message text-red-500 pl-2">
                {{ errorFormMessage["password"] }}
              </p>
            </div>

            <div class="flex flex-col gap-1">
              <label>
                <span class="text-red-500 text-xl">*</span>
                Confirm Password
              </label>
              <div class="relative">
                <input
                  name="confirmPassword"
                  :type="showConfirmPassword ? 'text' : 'password'"
                  class="input w-full"
                  placeholder="Password..."
                  v-model="userData.confirmPassword"
                  @focusin="handleFocusIn"
                  @focusout="handleFocusOut"
                />
                <button
                  type="button"
                  @click="toggleConfirmPassword"
                  class="absolute right-2 top-2 text-gray-500 cursor-pointer"
                >
                  <component
                    :is="showConfirmPassword ? EyeOff : Eye"
                    class="w-5 h-5"
                  />
                </button>
              </div>
              <p class="itbms-message text-red-500 pl-2">
                {{ errorFormMessage["confirmPassword"] }}
              </p>
            </div>

            <div class="flex flex-col gap-3 mt-5">
              <Button
                variant="primary"
                :disabled="isLoading || !isFormValid"
                class-name="itbms-save-button shadow-lg drop-shadow-[0_1px_1px_rgba(0,0,0,1)]"
                type="submit"
              >
                {{ isLoading ? "Loading..." : "Reset Password" }}
              </Button>
            </div>
          </form>
        </div>
      </div>

      <div v-else-if="currentState === 'SUCCESS'">
        <div
          class="px-16 py-32 flex flex-col gap-3 justify-center items-center"
        >
          <BadgeCheck class="text-green-500 size-20" />
          <h2 class="text-3xl">Reset Password Success</h2>
          <p class="text-lg text-gray-500">
            Your password has been successfully reset. You can now login
            again
            <RouterLink
              :to="{ name: 'login' }"
              class="text-purple-500 underline"
              >here.</RouterLink
            >
          </p>
        </div>
      </div>

      <div v-else-if="currentState === 'FAILED'">
        <div
          class="px-16 py-32 flex flex-col gap-3 justify-center items-center"
        >
          <BadgeX class="text-red-500 size-20" />
          <h2 class="text-3xl">Reset Password Failed</h2>
          <p class="text-lg text-gray-500">
            Invalid credential or reset time expires. Go back to
            <RouterLink
              :to="{ name: 'login' }"
              class="text-purple-500 underline"
              >Login.</RouterLink
            >
          </p>
        </div>
      </div>
    </div>
  </main>
</template>
