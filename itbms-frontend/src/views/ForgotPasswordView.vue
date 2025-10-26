<script setup>
import { requestResetPassword } from "@/api";
import Button from "@/components/Button.vue";
import { useStatusMessageStore } from "@/stores/statusMessage";
import { BadgeCheck, BadgeX, Eye, EyeOff } from "lucide-vue-next";
import { onMounted, reactive, ref, watch } from "vue";
import { RouterLink, useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const isLoading = ref(false);
const currentState = ref("REQUEST_RESET"); // REQUEST_RESET, SUCCESS, FAILED
const userData = reactive({
  email: "",
});

// Form Control
const currentFocusField = ref(null);
const isFormValid = ref(false);
const errorFormMessage = reactive({
  email: "",
});

const fieldIntegrity = {
  email: {
    checkConstraint: (data) => {
      return (
        0 < data.length &&
        data.length <= 50 &&
        /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(data)
      );
    },
    errorMessage: "Invalid email.",
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

const navigateToSigin = () => {
  router.push({ name: "login" });
};

watch(userData, () => {
  showErrorToForm();
  validateAllField();
});

// Request for Reset Password
const sendResetPasswordRequest = async (e) => {
    e.preventDefault();
  try {
    isLoading.value = true;
    const res = await requestResetPassword(userData.email);
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
      <div v-if="isLoading" class="text-center py-16">
        <div
          class="inline-block animate-spin rounded-full h-16 w-16 border-4 border-purple-200 border-t-purple-600 mb-4"
        ></div>
        <p class="text-gray-600 text-lg">Loading...</p>
      </div>

      <!-- Request for Reset Password (input email) -->
      <div v-else-if="currentState === 'REQUEST_RESET'">
        <div
          class="px-16 py-32 flex flex-col gap-3 justify-center items-center"
        >
          <h2 class="text-3xl text-center">Forgot Your Password?</h2>
          <p class="text-lg text-gray-500 text-center">
            Please enter your email address below to receive a password reset
            link.
          </p>

          <form
            @submit="sendResetPasswordRequest"
            class="flex flex-col gap-3 w-full max-w-xl"
          >
            <div class="flex flex-col gap-1">
              <label>
                <span class="text-red-500 text-xl">*</span>
                Email address
              </label>
              <div class="relative">
                <input
                  name="email"
                  type="email"
                  class="input w-full"
                  placeholder="Email..."
                  v-model="userData.email"
                  @focusin="handleFocusIn"
                  @focusout="handleFocusOut"
                />
              </div>
              <p class="itbms-message text-red-500 pl-2">
                {{ errorFormMessage["email"] }}
              </p>
            </div>

            <div class="flex flex-col gap-3 mt-5">
              <Button
                variant="primary"
                :disabled="isLoading || !isFormValid"
                class-name="itbms-save-button shadow-lg drop-shadow-[0_1px_1px_rgba(0,0,0,1)]"
                type="submit"
              >
                {{ isLoading ? "Loading..." : "Get Reset Link" }}
              </Button>
              <button
                  type="button"
                  @click="navigateToSigin"
                  class="text-purple-600 cursor-pointer hover:underline"
                >
                  Back to sign in
                </button>
            </div>
          </form>
        </div>
      </div>

      <div v-else-if="currentState === 'SUCCESS'">
        <div
          class="px-16 py-32 flex flex-col gap-3 justify-center items-center"
        >
          <BadgeCheck class="text-green-500 size-20" />
          <h2 class="text-3xl">Send Email Success</h2>
          <p class="text-lg text-gray-500">
            Reset link has been sent to your email. Please check your inbox.
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
            Something went wrong. Please try again or go back to
            <RouterLink
              :to="{ name: 'login' }"
              class="text-purple-500 underline"
              >Login.</RouterLink>
          </p>
        </div>
      </div>
    </div>
  </main>
</template>
