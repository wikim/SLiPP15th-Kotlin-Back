package net.slipp.fifth.kotlin.web.support.converter;

import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import com.google.common.collect.ImmutableSet;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.CodeableEnum;
import net.slipp.fifth.kotlin.member.MemberStatus;

@Slf4j
public class CodeableEnumConverter implements GenericConverter {

    @Getter
    private final Set<ConvertiblePair> convertibleTypes;

    public CodeableEnumConverter() {
        this.convertibleTypes = ImmutableSet.<ConvertiblePair>builder()
                .add(new ConvertiblePair(String.class, MemberStatus.class)).build();
    }

    @Override
    public Object convert(Object source, TypeDescriptor arg1, TypeDescriptor targetType) {
        String code = (String) source;
        log.debug("Find code: {} in {}", new Object[] { code, targetType.getType().getName() });

        @SuppressWarnings("unchecked")
        Class<? extends CodeableEnum> targetClass = (Class<? extends CodeableEnum>) targetType.getType();
        for (CodeableEnum codeableEnum : targetClass.getEnumConstants()) {
            if (codeableEnum.getCode().equals(code)) {
                return codeableEnum;
            }
        }
        throw new IllegalArgumentException("Unknown code '" + code + "' for enum type "
                + targetType.getType().getName());
    }
}
